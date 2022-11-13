<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="上级ID" prop="superiorId">
        <el-input
          v-model="queryParams.superiorId"
          placeholder="请输入上级ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item v-if="admin" label="酒店ID" prop="hotelId">
        <el-input
          v-model="queryParams.hotelId"
          placeholder="请输入酒店ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="部门名" prop="departmentName">
        <el-input
          v-model="queryParams.departmentName"
          placeholder="请输入部门名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['business:department:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['business:department:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:department:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['business:department:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="departmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="部门ID" align="center" prop="departmentId" />
      <el-table-column label="上级ID" align="center" prop="superiorId" />
      <el-table-column label="酒店ID" align="center" prop="hotelId" />
      <el-table-column label="部门名" align="center" prop="departmentName" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:department:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:department:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改部门信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="上级ID" prop="superiorId">
          <el-input v-model="form.superiorId" placeholder="请输入上级ID" />
        </el-form-item>
        <el-form-item v-if="admin" label="酒店ID" prop="hotelId">
          <el-input v-model="form.hotelId" placeholder="请输入酒店ID" />
        </el-form-item>
        <el-form-item label="部门名" prop="departmentName">
          <el-input v-model="form.departmentName" placeholder="请输入部门名" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-divider content-position="center">员工信息信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddBaseStaff">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteBaseStaff">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="baseStaffList" :row-class-name="rowBaseStaffIndex" @selection-change="handleBaseStaffSelectionChange" ref="baseStaff">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="员工名" prop="staffName" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.staffName" placeholder="请输入员工名" />
            </template>
          </el-table-column>
          <el-table-column label="联系电话" prop="staffPhone" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.staffPhone" placeholder="请输入联系电话" />
            </template>
          </el-table-column>
          <el-table-column v-if="admin" label="酒店ID" prop="hotelId" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.hotelId" placeholder="请输入酒店ID" />
            </template>
          </el-table-column>
          <el-table-column label="员工密码" prop="staffPassword" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.staffPassword" placeholder="请输入员工密码" />
            </template>
          </el-table-column>
          <el-table-column label="备注" prop="remark" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.remark" placeholder="请输入备注" />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDepartment, getDepartment, delDepartment, addDepartment, updateDepartment } from "@/api/business/department";
import Cookies from "js-cookie";

export default {
  name: "Department",
  data() {
    return {
      //超级管理员
      admin: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedBaseStaff: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 部门信息表格数据
      departmentList: [],
      // 员工信息表格数据
      baseStaffList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        superiorId: null,
        hotelId: null,
        departmentName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        hotelId: [
          { required: true, message: "酒店ID不能为空", trigger: "blur" }
        ],
        departmentName: [
          { required: true, message: "部门名不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    //0:酒店管理员
    if(Cookies.get("admin") === "1")
      this.admin = true;
  },
  methods: {
    /** 查询部门信息列表 */
    getList() {
      this.loading = true;
      listDepartment(this.queryParams).then(response => {
        this.departmentList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        departmentId: null,
        superiorId: null,
        hotelId: null,
        departmentName: null,
        remark: null
      };
      this.baseStaffList = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.departmentId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加部门信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const departmentId = row.departmentId || this.ids
      getDepartment(departmentId).then(response => {

        this.form = response.data;
        this.baseStaffList = response.data.baseStaffList;
        this.open = true;
        this.title = "修改部门信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.baseStaffList = this.baseStaffList;
          if (this.form.departmentId != null) {
            //修改
            updateDepartment(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            //新增
            addDepartment(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const departmentIds = row.departmentId || this.ids;
      this.$modal.confirm('是否确认删除部门信息编号为"' + departmentIds + '"的数据项？').then(function() {
        return delDepartment(departmentIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
	/** 员工信息序号 */
    rowBaseStaffIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 员工信息添加按钮操作 */
    handleAddBaseStaff() {
      let obj = {};
      obj.staffName = "";
      obj.staffPhone = "";
      obj.hotelId = "";
      obj.staffPassword = "";
      obj.remark = "";
      this.baseStaffList.push(obj);
    },
    /** 员工信息删除按钮操作 */
    handleDeleteBaseStaff() {
      if (this.checkedBaseStaff.length == 0) {
        this.$modal.msgError("请先选择要删除的员工信息数据");
      } else {
        const baseStaffList = this.baseStaffList;
        const checkedBaseStaff = this.checkedBaseStaff;
        this.baseStaffList = baseStaffList.filter(function(item) {
          return checkedBaseStaff.indexOf(item.index) == -1
        });
      }
    },
    /** 复选框选中数据 */
    handleBaseStaffSelectionChange(selection) {
      this.checkedBaseStaff = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('business/department/export', {
        ...this.queryParams
      }, `department_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="员工名" prop="staffName">
        <el-input
          v-model="queryParams.staffName"
          placeholder="请输入员工名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系电话" prop="staffPhone">
        <el-input
          v-model="queryParams.staffPhone"
          placeholder="请输入联系电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="酒店ID" v-if="admin" prop="hotelId">
        <el-input
          v-model="queryParams.hotelId"
          placeholder="请输入酒店ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="部门" prop="departmentId">
        <el-select v-model="queryParams.departmentId" filterable placeholder="请选择部门" @change="departmentChange">
          <el-option
            v-for="d in baseDepartmentList"
            :label="d.departmentName"
            :key="d.departmentId"
            :value="parseInt(d.departmentId)"
          ></el-option>
        </el-select>
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
          v-hasPermi="['business:staff:add']"
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
          v-hasPermi="['business:staff:edit']"
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
          v-hasPermi="['business:staff:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['business:staff:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="staffList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="员工名" align="center" prop="staffName" />
      <el-table-column label="联系电话" align="center" prop="staffPhone" />
      <el-table-column label="酒店" align="center" prop="hotelName" />
      <el-table-column label="部门名" align="center" prop="departmentName" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:staff:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:staff:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-lock"
            @click="resetPassword(scope.row)"
            v-hasPermi="['business:staff:edit']"
          >重置密码</el-button>
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

    <!-- 添加或修改员工信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="员工名" prop="staffName">
          <el-input v-model="form.staffName" placeholder="请输入员工名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="staffPhone">
          <el-input v-model="form.staffPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="部门" prop="departmentId">
          <el-select v-model="form.departmentId" filterable placeholder="请选择部门" @change="departmentChange">
            <el-option
              v-for="d in baseDepartmentList"
              :label="d.departmentName"
              :key="d.departmentId"
              :value="parseInt(d.departmentId)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="员工密码" v-if="this.form.staffId === null" prop="staffPassword">
          <el-input type="password"  show-password v-model="form.staffPassword" placeholder="请输入员工密码" />
        </el-form-item>

        <el-form-item label="角色">
          <el-select v-model="form.roles" multiple placeholder="请选择角色"  @change="$forceUpdate()">
            <el-option
              v-for="item in roleList"
              :key="item.roleId"
              :label="item.roleName"
              :value="parseInt(item.roleId)"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>



        <el-divider content-position="center">职位信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddBasePosition">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteBasePosition">删除</el-button>
          </el-col>
        </el-row>

        <el-table :data="basePositionList" :row-class-name="rowBasePositionIndex" @selection-change="handleBasePositionSelectionChange" ref="basePosition">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>

          <el-table-column label="职位名" prop="positionId" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.positionId" filterable>
                <el-option v-for="i in allBasePositionList"
                           :key="i.positionId"
                           :label="i.positionName"
                           :value="i.positionId">
                </el-option>
              </el-select>
            </template>
          </el-table-column>


          <el-table-column label="备注" prop="remark" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.remark" disabled="disabled" />
            </template>
          </el-table-column>
          <el-table-column label="职能" prop="functionValue" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.functionValue" disabled="disabled" />
            </template>
          </el-table-column>

        </el-table>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>




    <!-- 重置密码对话框 -->
    <el-dialog :title="title" :visible.sync="resetOpen" width="600px" append-to-body>
      <el-form ref="form" :model="form" label-width="120px">

        <el-form-item label="请输入新密码" prop="staffPassword">
          <el-input type="password" show-password v-model="form.staffPassword" placeholder="请输入员工密码" />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitResetForm">确 定</el-button>
        <el-button @click="cancelReset">取 消</el-button>
      </div>
    </el-dialog>




  </div>
</template>

<script>
import {listStaff, getStaff, delStaff, addStaff, updateStaff, updateStaffPassword} from "@/api/business/staff";
import Cookies from "js-cookie";
import {listAllPosition, listPositionByDepartmentId} from "@/api/business/position";
import {listDepartment} from "@/api/business/department";

export default {
  name: "Staff",
  data() {
    return {
      admin: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 员工信息表格数据
      staffList: [],
      //角色列表
      roleList: [],
      // 员工职位信息列表
      basePositionList: [],
      //员工能选的职位信息
      allBasePositionList: [],
      //部门列表
      baseDepartmentList: [],
      //原本的部门ID和职位列表
      originalMessage: {
        departmentId: "",
        list: []
      },
      //记录全部
      allList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //重置密码弹出层
      resetOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        staffName: null,
        staffPhone: null,
        hotelId: null,
        departmentId: null,
        staffPassword: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        staffName: [
          { required: true, message: "员工名不能为空", trigger: "blur" }
        ],
        staffPhone: [
          { required: true, message: "联系电话不能为空", trigger: "blur" }
        ],
        hotelId: [
          { required: true, message: "酒店ID不能为空", trigger: "blur" }
        ],
        departmentId: [
          { required: true, message: "部门不能为空", trigger: "blur" }
        ],
        staffPassword: [
          { required: true, message: "员工密码不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {

    this.getList();
    listDepartment().then(res => {
      this.baseDepartmentList = res.data;
    })
    //0:酒店管理员
    if(Cookies.get("admin") === "1")
      this.admin = true;
  },
  methods: {
    /** 查询员工信息列表 */
    getList() {
      this.loading = true;
      listStaff(this.queryParams).then(response => {
        this.staffList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 员工的部门改变时改变职位 */
    departmentChange(id) {
      if(id === this.originalMessage.departmentId) {
        this.basePositionList = this.originalMessage.list;
      }
      else {
        this.basePositionList = [];
      }

      listPositionByDepartmentId(id).then(res => {
        this.allBasePositionList = res;
      })
    },
    /** 复选框选中数据 */
    handleBaseStaffSelectionChange(selection) {
      this.checkedBaseStaff = selection.map(item => item.index)
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    //重置取消
    cancelReset() {
      this.resetOpen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        staffId: null,
        staffName: null,
        staffPhone: null,
        hotelId: null,
        departmentId: null,
        staffPassword: null,
        remark: null
      };
      this.basePositionList = [];
      this.originalMessage.departmentId = "";
      this.originalMessage.list = [];
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
      this.ids = selection.map(item => item.staffId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      getStaff().then(response => {
        this.roleList = response.roleList;
      })
      this.title = "添加员工信息";
    },
    /** 职位信息添加按钮操作 */
    handleAddBasePosition() {
      let obj = {};
      obj.positionName = "";
      obj.remark = "";
      obj.functionValue = "";
      this.basePositionList.push(obj);
    },
    /** 职位信息序号 */
    rowBasePositionIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 复选框选中数据 */
    handleBasePositionSelectionChange(selection) {
      this.checkedBasePosition = selection.map(item => item.index)
    },
    /** 职位信息删除按钮操作 */
    handleDeleteBasePosition() {
      if (this.checkedBasePosition.length === 0) {
        this.$modal.msgError("请先选择要删除的职位信息数据");
      } else {
        const basePositionList = this.basePositionList;
        const checkedBasePosition = this.checkedBasePosition;
        this.basePositionList = basePositionList.filter(function(item) {
          return checkedBasePosition.indexOf(item.index) === -1
        });
      }
    },
    /** 重置密码 */
    resetPassword(row) {
      this.reset();
      const staffId = row.staffId || this.ids

      getStaff(staffId).then(response => {
        this.form = response.data;
        this.form.staffPassword = "";
        this.resetOpen = true;
        this.title = "重置密码";
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const staffId = row.staffId || this.ids


      listPositionByDepartmentId(row.departmentId).then(res => {
        this.allBasePositionList = res;
      })

      getStaff(staffId).then(response => {
        this.form = response.data;
        this.form.roles = response.roles;
        this.roleList = response.roleList;

        this.basePositionList = response.data.basePositionList;
        this.originalMessage.departmentId = row.departmentId;
        this.originalMessage.list = this.basePositionList;
        this.open = true;
        this.title = "修改员工信息";
      });
    },
    /** 提交按钮 */
    submitForm() {

      for(var i = 0; i < this.basePositionList.length; i++) {
        if(this.basePositionList[i].positionId === undefined) {
          this.$modal.msgError("职位不能为空");
          return;
        }
      }

      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.basePositionList = this.basePositionList;
          if (this.form.staffId != null) {
            updateStaff(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addStaff(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    submitResetForm() {
      if(this.form.staffPassword === null || this.form.staffPassword === "") {
        this.$modal.msgError("密码不能为空");
        return;
      }

      if(this.form.staffPassword.length < 5 || this.form.staffPassword.length > 20) {
        this.$modal.msgError("密码长度必须介于 5 和 20 之间");
        return;
      }



      updateStaffPassword(this.form).then(response => {
        this.$modal.msgSuccess("重置成功");
        this.resetOpen = false;
        this.getList();
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const staffIds = row.staffId || this.ids;
      this.$modal.confirm('是否确认删除员工信息编号为"' + staffIds + '"的数据项？').then(function() {
        return delStaff(staffIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('business/staff/export', {
        ...this.queryParams
      }, `staff_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

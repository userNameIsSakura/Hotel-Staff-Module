<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
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
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="departmentList"
      row-key="departmentId"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
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
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['business:department:add']"
          >新增</el-button>
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


    <!-- 添加或修改部门信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">

        <el-form-item label="上级部门" prop="superiorId">
          <el-select v-model="form.superiorId" filterable placeholder="请输入上级ID" >
            <el-option
              v-for="d in allDepartmentList"
              :key="d.departmentId"
              :label="d.departmentName"
              :value="parseInt(d.departmentId)"
              v-if="!nowAndLower.includes(d.departmentId)"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="部门名" prop="departmentName">
          <el-input v-model="form.departmentName" placeholder="请输入部门名" />
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
              <el-input v-model="scope.row.remark" placeholder="请输入备注" />
            </template>
          </el-table-column>
          <el-table-column label="职能" prop="functionValue" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.functionValue" placeholder="请输入职能" />
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
import {listAllPosition, listPosition} from "@/api/business/position";

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
      checkedBasePosition: [],
      // 职位列表
      allBasePositionList: [],
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
      // 全部部门信息表格
      allDepartmentList: [],
      //当前部门及其下级ID
      nowAndLower: [],
      // 职位信息表格数据
      basePositionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 部门信息树选项
      departmentOptions: [],
      // 查询参数
      queryParams: {
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
        this.departmentList = this.handleTree(response.data, "departmentId", "superiorId");

        this.allDepartmentList = response.data;

        listAllPosition().then(res => {
          this.allBasePositionList = res;
        })

        this.allDepartmentList.push({
          departmentId: -1,
          departmentName: "无上级部门",
        });
        this.loading = false;
      });
    },

    /** 转换部门信息数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.departmentId,
        label: node.departmentName,
        children: node.children
      };
    },
    /** 查询部门信息下拉树结构 */
    getTreeselect() {
      listDepartment().then(response => {
        this.departmentOptions = [];
        const data = { departmentId: 0, departmentName: '顶级节点', children: [] };
        data.children = this.handleTree(response.data, "departmentId", "superiorId");
        this.departmentOptions.push(data);
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
      this.basePositionList = [];
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
    handleAdd(row) {
      this.reset();
      this.form.superiorId = isNaN(parseInt(row.departmentId)) ? -1 : parseInt(row.departmentId);
      this.open = true;
      this.title = "添加部门信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();

      //排除不可作为上级的部门
      var list = [row.departmentId];
      var all = this.allDepartmentList;
      var end = true;
      while (end) {
        end = false;
        all.forEach(a => {
          if(list.includes(a.superiorId) && !list.includes(a.departmentId)) {
            list.push(a.departmentId);
          }
        })
      }
      this.nowAndLower = list;

      const departmentId = row.departmentId || this.ids
      getDepartment(departmentId).then(response => {

        this.form = response.data;
        this.basePositionList = response.data.basePositionList;
        this.open = true;
        this.title = "修改部门信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      var list = [];
      for(var i = 0;i < this.basePositionList.length; i++) {
        if(this.basePositionList[i].functionValue === "" || this.basePositionList[i].positionId === undefined) {
          this.$modal.msgError("职位/职能不能为空");
          return;
        }
        if(list.includes(this.basePositionList[i].positionId)) {
          this.$modal.msgError("职位不能重复");
          return;
        }

        list.push(this.basePositionList[i].positionId);
      }

      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.basePositionList = this.basePositionList;
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
    rowBasePositionIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 员工信息添加按钮操作 */
    handleAddBasePosition() {
      let obj = {};
      obj.positionName = "";
      obj.remark = "";
      obj.functionValue = "";
      this.basePositionList.push(obj);
    },
    /** 员工信息删除按钮操作 */
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
    /** 复选框选中数据 */
    handleBasePositionSelectionChange(selection) {
      this.checkedBasePosition = selection.map(item => item.index)
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

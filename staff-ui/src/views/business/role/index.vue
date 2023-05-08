<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">

      <el-form-item label="酒店选择" prop="entityHotel">
        <el-select v-model="queryParams.hotelId" placeholder="酒店选择">
          <el-option
            v-for="dict in hotels"
            :key="dict.chotelId"
            :label="dict.chotelName"
            :value="dict.chotelId"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="角色名" prop="roleName">
        <el-input
          v-model="queryParams.roleName"
          placeholder="请输入角色名"
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
          v-hasPermi="['business:role:add']"
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
          v-hasPermi="['business:role:edit']"
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
          v-hasPermi="['business:role:remove']"
        >删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['business:role:export']"
        >导出</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="roleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="角色名" align="center" prop="roleName" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:role:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:role:remove']"
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

    <!-- 添加或修改角色信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="角色名" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名" />
        </el-form-item>

        <el-form-item label="权限">
          <el-select v-model="form.auths" multiple placeholder="请选择权限"  @change="$forceUpdate()">
            <el-option
              v-for="item in authList"
              :key="item.authId"
              :label="item.authName"
              :value="parseInt(item.authId)"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRole, getRole, delRole, addRole, updateRole } from "@/api/business/role";
import {listChainHotel} from "@/api/business/chainHotel";

export default {
  name: "Role",
  data() {
    return {
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
      // 角色信息表格数据
      roleList: [],
      //权限集合
      authList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        roleName: null,
        hotelId: null
      },
      hotels: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        roleName: [
          { required: true, message: "角色名不能为空", trigger: "blur" }
        ],
        hotelId: [
          { required: true, message: "酒店ID不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getEntityHotels();
  },
  methods: {
    /** 查询角色信息列表 */
    getList() {
      this.loading = true;
      listRole(this.queryParams).then(response => {
        this.roleList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getEntityHotels() {
      var hotel =  [];
      listChainHotel().then(res => {
        hotel = res.data
        if(hotel[0].chotelType === 0) {
          // 连锁
          if(hotel[0].chotelParent === 0)
            this.hotels = hotel.slice(1);
          else
            this.hotels = hotel;
        }else {
          // 单体
          this.hotels = hotel;
        }
        this.queryParams.hotelId = this.hotels[0].chotelId;
        this.getList();
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
        roleId: null,
        roleName: null,
        hotelId: null
      };
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
      this.ids = selection.map(item => item.roleId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      getRole(this.queryParams.hotelId,null).then(response => {
        this.authList = response.authList;
      })
      this.open = true;
      this.title = "添加角色信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const roleId = row.roleId || this.ids
      getRole(this.queryParams.hotelId,roleId).then(response => {
        this.form = response.data;
        this.form.auths = response.auths;
        this.authList = response.authList;
        this.open = true;
        this.title = "修改角色信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.hotelId = this.queryParams.hotelId;
          if (this.form.roleId != null) {
            updateRole(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRole(this.form).then(response => {
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
      const roleIds = row.roleId || this.ids;
      this.$modal.confirm('是否确认删除角色信息编号为"' + roleIds + '"的数据项？').then(function() {
        return delRole(roleIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('business/role/export', {
        ...this.queryParams
      }, `role_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

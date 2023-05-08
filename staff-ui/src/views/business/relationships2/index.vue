<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="酒店" prop="hotelId">
        <el-select v-model="queryParams.hotelId" filterable placeholder="请选择酒店">
          <el-option
            v-for="h in hotelList"
            :key="h.hotelId"
            :label="h.hotelName"
            :value="h.hotelId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="订阅" prop="subscribeId">
        <el-select v-model="queryParams.subscribeId" filterable placeholder="请选择订阅">
          <el-option
            v-for="s in subscribeList"
            :key="s.subscribeId"
            :label="s.subscribeContent"
            :value="s.subscribeId">
          </el-option>
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
          v-hasPermi="['business:relationships2:add']"
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
          v-hasPermi="['business:relationships2:edit']"
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
          v-hasPermi="['business:relationships2:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['business:relationships2:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="relationships2List" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="酒店" align="center">
        <template slot-scope="scope">
          {{getHotelName(scope.row)}}
        </template>
      </el-table-column>
      <el-table-column label="订阅" align="center" >
        <template slot-scope="scope">
          {{getSubscribeName(scope.row)}}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:relationships2:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:relationships2:remove']"
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

    <!-- 添加或修改订阅黑名单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="酒店" prop="hotelId">
          <el-select v-model="form.hotelId" filterable placeholder="请选择酒店">
            <el-option
              v-for="h in hotelList"
              :key="h.hotelId"
              :label="h.hotelName"
              :value="h.hotelId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="订阅" prop="subscribeId">
          <el-select v-model="form.subscribeId" filterable placeholder="请选择订阅">
            <el-option
              v-for="s in subscribeList"
              :key="s.subscribeId"
              :label="s.subscribeContent"
              :value="s.subscribeId">
            </el-option>
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
import {
  listRelationships2,
  getRelationships2,
  delRelationships2,
  addRelationships2,
  updateRelationships2,
  listSubscribeAll
} from "@/api/business/relationships2";
import {listHotelAll} from "@/api/business/hotel";

export default {
  name: "Relationships2",
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
      // 订阅黑名单表格数据
      relationships2List: [],
      //酒店列表
      hotelList: [],
      //订阅列表
      subscribeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        hotelId: null,
        subscribeId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        hotelId: [
          { required: true, message: "酒店不能为空", trigger: "blur" }
        ],
        subscribeId: [
          { required: true, message: "订阅不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {

    listHotelAll().then(res => {
      this.hotelList = res;
    })
    listSubscribeAll().then(res => {
      this.subscribeList = res;
    })
    this.getList();
  },
  methods: {
    getHotelName(row) {
      for(var i = 0; i < this.hotelList.length; i++) {
        var h = this.hotelList[i];
        if(h.hotelId === parseInt(row.hotelId)) {
          return h.hotelName;
        }
      }
      return "error";
    },
    getSubscribeName(row) {
      for(var i = 0; i < this.subscribeList.length; i++) {
        var s = this.subscribeList[i];
        if(s.subscribeId === row.subscribeId) {
          return s.subscribeContent;
        }
      }
      return "error";

    },
    /** 查询订阅黑名单列表 */
    getList() {
      this.loading = true;
      listRelationships2(this.queryParams).then(response => {
        this.relationships2List = response.rows;
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
        id: null,
        hotelId: null,
        subscribeId: null
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加订阅黑名单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getRelationships2(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改订阅黑名单";
      });
    },
    /** 提交按钮 */
    submitForm() {

      for(var i = 0; i < this.relationships2List.length; i++) {
        var r = this.relationships2List[i];
        if(r.subscribeId === this.form.subscribeId && r.hotelId === this.form.hotelId) {
          this.$modal.msgError("不可重复添加黑名单！");
          return;
        }
      }

      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRelationships2(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRelationships2(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除订阅黑名单编号为"' + ids + '"的数据项？').then(function() {
        return delRelationships2(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('business/relationships2/export', {
        ...this.queryParams
      }, `relationships2_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

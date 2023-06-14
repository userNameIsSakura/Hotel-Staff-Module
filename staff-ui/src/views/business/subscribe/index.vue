<template>
  <div class="app-container">

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订阅内容" prop="parameter">
        <el-input
          v-model="queryParams.subscribeContent"
          placeholder="请输入订阅内容"
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
          v-hasPermi="['business:subscribe:add']"
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
          v-hasPermi="['business:subscribe:edit']"
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
          v-hasPermi="['business:subscribe:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['business:subscribe:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="subscribeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订阅内容" align="center" prop="subscribeContent" />
      <el-table-column label="是否可用" align="center" >
        <template slot-scope="scope">
          {{scope.row.available === true ? "启用" : "停用"}}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:subscribe:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:subscribe:remove']"
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

    <!-- 添加或修改订阅信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订阅内容">
          <el-input v-model="form.subscribeContent" :min-height="192"/>
        </el-form-item>

        <el-form-item label="是否启用" prop="available">
          <el-switch v-model="form.available"></el-switch>
        </el-form-item>
      </el-form>

      <el-divider content-position="center">限制参数</el-divider>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddParam">添加</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteParam">删除</el-button>
        </el-col>
      </el-row>


      <el-table :data="paramList" :row-class-name="rowParamIndex" @selection-change="handleParamSelectionChange" ref="baseParam">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="序号" align="center" prop="index" width="50"/>

        <el-table-column label="KEY" prop="remark" width="150">
          <template slot-scope="scope">
            <el-input v-model="scope.row.key" />
          </template>
        </el-table-column>

        <el-table-column label="VALUE" width="250">
          <template slot-scope="scope">
            <div style="display: flex; align-items: center;">
              <el-select v-model="scope.row.valueType" style="margin-right: 10px;">
                <el-option label="字符串" value="string" selected="true"></el-option>
                <el-option label="数字" value="number"></el-option>
              </el-select>
              <el-input v-model="scope.row.value" :clearable="true" v-if="scope.row.valueType === 'string'" type="text" />
              <el-input v-model.number="scope.row.value" :clearable="true" v-if="scope.row.valueType === 'number'" type="number"/>
            </div>
          </template>
        </el-table-column>
      </el-table>


      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listSubscribe, getSubscribe, delSubscribe, addSubscribe, updateSubscribe } from "@/api/business/subscribe";

export default {
  name: "Subscribe",
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
      // 订阅信息表格数据
      subscribeList: [],
      // 订阅限制参数数据
      paramList: [],
      // 弹出层标题
      title: "",
      checkedParam: [],
      valueType: "text",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        subscribeContent: null,
        parameter: null,
        available: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        subscribeContent: [
          { required: true, message: "订阅内容不能为空", trigger: "blur" }
        ],
        available: [
          { required: true, message: "是否可用不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询订阅信息列表 */
    getList() {
      this.loading = true;
      listSubscribe(this.queryParams).then(response => {
        this.subscribeList = response.rows;
        this.subscribeList.forEach( s => {
          s.available = s.available === 1;
        })
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
        subscribeId: null,
        subscribeContent: null,
        parameter: null,
        available: true,
        paramList: []
      };

      this.paramList = [];
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
      this.ids = selection.map(item => item.subscribeId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加订阅信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const subscribeId = row.subscribeId || this.ids

      getSubscribe(subscribeId).then(response => {
        response.data.available = response.data.available === 1;
        this.form = response.data;
        this.open = true;
        this.title = "修改订阅信息";


        this.paramList = [];
        if(!(this.form.parameter === null || this.form.parameter === "")) {
          var parse = JSON.parse(this.form.parameter);
          var list = [];
          var keys = Object.keys(parse);
          for (let key of keys) {
            var tem = {
              key:"",
              value:"",
              valueType:"string"
            };
            tem.key = key;
            tem.value = Reflect.get(parse,key);
            tem.valueType = typeof tem.value === "number" ? "number" : "string";
            list.push(tem);
          }
          console.log(list);
          this.paramList = list;
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {

          for(let param of this.paramList) {
            if(param.key === "" || param.value === "") {
              this.$message.error(param.index + "号数据存在空值");
              return;
            }

            if(param.valueType === "number" && typeof param.value !== "number") {
              if(!isNaN(param.value)) {
                param.value = parseInt(param.value);
              }else {
                this.$message.error(param.index + "号数据值非数字格式");
                return;
              }
            }
          }

          if(this.form.subscribeContent === "" || this.form.subscribeContent === null) {
            this.$message.error("订阅内容不得为空");
            return;
          }

          if(this.form.available) {
            this.form.available = 1;
          }else {
            this.form.available = 0;
          }

          this.form.paramList = this.paramList;

          if (this.form.subscribeId != null) {
            updateSubscribe(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSubscribe(this.form).then(response => {
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
      const subscribeIds = row.subscribeId || this.ids;
      this.$modal.confirm('是否确认删除订阅信息编号为"' + subscribeIds + '"的数据项？').then(function() {
        return delSubscribe(subscribeIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('business/subscribe/export', {
        ...this.queryParams
      }, `subscribe_${new Date().getTime()}.xlsx`)
    },
    /** 职位信息添加按钮操作 */
    handleAddParam() {
      let obj = {};
      obj.key = "";
      obj.value = "";
      this.paramList.push(obj);
    },
    /** 复选框选中数据 */
    handleParamSelectionChange(selection) {
      this.checkedParam = selection.map(item => item.index)
    },
    /** 职位信息删除按钮操作 */
    handleDeleteParam() {
      if (this.checkedParam.length === 0) {
        this.$modal.msgError("请先选择要删除的数据");
      } else {
        const paramList = this.paramList;
        const checkedParam = this.checkedParam;
        this.paramList = paramList.filter(function(item) {
          return checkedParam.indexOf(item.index) === -1
        });
      }
    },
    /** 职位信息序号 */
    rowParamIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
  }
};
</script>

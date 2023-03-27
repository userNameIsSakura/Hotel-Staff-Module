<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['business:chainHotel:list']"
        >新增</el-button>
      </el-col>
<!--      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:staff:remove']"
        >删除</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="modelList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="范本名" align="center" prop="modelName" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="preview(scope.row)"
          >预览</el-button>

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

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" label-width="120px">

        <el-form-item label="请输入合同名" prop="modelName">
          <el-input type="text" v-model="form.modelName" placeholder="请输入合同名" />
        </el-form-item>

        <file-upload v-model="form.modelFile" limit="1"></file-upload>
<!--        <file-upload file-type=["pdf"] limit="1"></file-upload>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listModel, getModel, delModel, addModel, updateModel } from "@/api/business/model";
import {getStaff} from "@/api/business/staff";

export default {
  name: "Model",
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
      // 范本列表表格数据
      modelList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        modelName: null,
        modelFile: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        modelName: [
          { required: true, message: "范本名不能为空", trigger: "blur" }
        ],
        modelFile: [
          { required: true, message: "范本文件地址不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询范本列表列表 */
    getList() {
      this.loading = true;
      listModel(this.queryParams).then(response => {
        this.modelList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加合同模板";
    },
    reset() {
      this.form = {
        modelName : null,
        modelFile : null
      }
    },
    submitForm() {
      if(this.form.modelName === null || this.form.modelName === undefined || this.form.modelName === "") {
        this.$modal.msgError("合同名称不得为空");
        return;
      }

      if(this.form.modelFile === null) {
        this.$modal.msgError("合同文件不得为空");
        return;
      }
      addModel(this.form).then(res => {
        this.$modal.msgSuccess("新增成功");
        this.open = false;
        this.getList();
      })
    },
    preview(row) {
      this.$router.push({path:"/electronicContract",query: {modelId:row.modelId}})
    }


  }
};
</script>

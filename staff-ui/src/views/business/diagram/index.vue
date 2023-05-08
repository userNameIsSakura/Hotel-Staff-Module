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

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
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
          v-hasPermi="['business:diagram:add']"
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
          v-hasPermi="['business:diagram:edit']"
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
          v-hasPermi="['business:diagram:remove']"
        >删除</el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="diagramList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="介绍图类别" align="center" prop="diagramType" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:diagram:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:diagram:remove']"
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

    <!-- 添加或修改酒店介绍图对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="介绍图类别" prop="diagramType">
          <el-select placeholder="请选择介绍图类别" filterable v-model="form.diagramType">
            <el-option
              v-for="t in diagramTypeList"
              :key="t.diagramType"
              :label="t.diagramType"
              :value="t.diagramType">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="介绍图" prop="diagramPath">
          <image-upload v-model="form.diagramPath" limit="20" file-size="100" >
          </image-upload>
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
import { listDiagram, getDiagram, delDiagram, addDiagram, updateDiagram } from "@/api/business/diagram";
import {listDiagramType} from "@/api/business/diagramType";
import {listChainHotel} from "@/api/business/chainHotel";

export default {
  name: "Diagram",
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
      // 酒店介绍图表格数据
      diagramList: [],
      // 酒店介绍图类别
      diagramTypeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        hotelId: null,
        diagramType: null,
        diagramPath: null,
      },
      hotels: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        hotelId: [
          { required: true, message: "酒店ID不能为空", trigger: "blur" }
        ],
        diagramType: [
          { required: true, message: "介绍图类别不能为空", trigger: "change" }
        ],
        diagramPath: [
          { required: true, message: "介绍图路径不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getEntityHotels();
  },
  methods: {
    beforeUpload(file) {
      return false;
    },
    /** 查询酒店介绍图列表 */
    getList() {
      this.loading = true;

      listDiagramType().then(response => {
          this.diagramTypeList = response.rows;
        }
      )
      listDiagram(this.queryParams).then(response => {
        this.diagramList = response.rows;
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
        id: null,
        hotelId: null,
        diagramType: null,
        diagramPath: null,
        remark: null
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
      this.title = "添加酒店介绍图";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDiagram(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改酒店介绍图";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {

          this.form.hotelId = this.queryParams.hotelId;
          if (this.form.id != null) {
            updateDiagram(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDiagram(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除酒店介绍图编号为"' + ids + '"的数据项？').then(function() {
        return delDiagram(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('business/diagram/export', {
        ...this.queryParams
      }, `diagram_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

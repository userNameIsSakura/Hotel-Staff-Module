<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="集团名" prop="chotelName">
        <el-input
          v-model="queryParams.chotelName"
          placeholder="请输入集团名"
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
          v-hasPermi="['business:chainHotel:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="toggleExpandAll"
        >展开/折叠</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="chainHotelList"
      row-key="chotelId"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column label="公司/集团" align="center" header-align="center" prop="chotelName" />
        <el-table-column label="属性" align="center" header-align="center" prop="chotelType">
          <template slot-scope="scope">
            <span v-if="scope.row.chotelType === 1">连锁酒店集团</span>
            <span v-if="scope.row.chotelType === 0">个体酒店集团</span>
          </template>
        </el-table-column>
      <el-table-column label="操作" align="right"  class-name="small-padding fixed-width">

        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAddHotel(scope.row)"
            v-hasPermi="['business:chainHotel:add']"
            v-if="scope.row.chotelParent === null || scope.row.chotelParent === 0"
          >新增</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:chainHotel:edit']"
          >修改</el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:chainHotel:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

<!--     添加或修改连锁酒店对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
          <el-form  ref="form" :model="form" :rules="rules" label-width="120px">
            <el-form-item label="公司/集团名" prop="chotelName" >
              <el-input v-model="form.chotelName" placeholder="请输入酒店名" />
            </el-form-item>
            <el-form-item label="属性" prop="chotelType" >
              <el-select v-model="form.chotelType" placeholder="请选择属性">
                <el-option label="连锁酒店集团" :value="1"/>
                <el-option label="个体酒店集团" :value="0"/>
              </el-select>
            </el-form-item>
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入备注" />
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
  listChainHotel,
  getChainHotel,
  delChainHotel,
  addChainHotel,
  updateChainHotel,
} from "@/api/business/chainHotel";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {addHotel, getHotel, getHotelByCHotelId, updateHotel} from "@/api/business/hotel";
import region from "@/api/business/region";

export default {
  name: "ChainHotel",
  components: {
    Treeselect
  },
  dicts: ["hotel_settlement"],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 连锁酒店表格数据
      chainHotelList: [],
      // 连锁酒店树选项
      chainHotelOptions: [],
      // 全部介绍图类别

      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      formOpen: false,
      diagramOpen: false,
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 查询参数
      queryParams: {
        chotelParent: null,
        chotelName: null,
      },
      // 表单参数
      form: {},
      // 酒店表单参数
      hotelForm: {},
      // 介绍图表单参数
      diagramForm: {},
      // 地图
      map: null,
      local: null,
      markerLayer: null,
      // 表单校验
      rules: {
        chotelName: [
          { required: true, message: "酒店名不能为空", trigger: "blur" }
        ],
      },
      diagramRules: {
        diagramType: [
          { required: true, message: "介绍图类型不能为空", trigger: "blur" }
        ],
      },
      rulesForm: {
        hotelName: [
          { required: true, message: "酒店名不能为空", trigger: "blur" }
        ],
        hotelRoomCards: [
          { required: true, message: "房卡数量不能为空", trigger: "blur" }
        ],
        hotelNumber: [
          { required: true, message: "酒店编号不能为空", trigger: "blur" }
        ],
        hotelSettlement: [
          { required: true, message: "结算时间不能为空", trigger: "blur" }
        ],
        hotelFreeBreakfast: [
          { required: true, message: "免费早餐数量不能为空", trigger: "blur" }
        ],
        hotelDeposit: [
          { required: true, message: "押金不能为空", trigger: "blur" }
        ],
        hotelPoster: [
          { required: true, message: "海报不能为空", trigger: "blur" }
        ],
        hotelIntroduct: [
          { required: true, message: "酒店简介不能为空", trigger: "blur" }
        ],
        chotelId: [
          { required: true, message: "关联ID不能为空", trigger: "blur" }
        ]
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    select(data) {
      if (this.hotelForm.hotelNumber === null) {
        this.hotelForm.hotelNumber = data.area.code + "0000";
      } else {
        if (this.hotelForm.hotelNumber.length === 10)
          var id = this.hotelForm.hotelNumber.slice(6, 10);
        else if (this.hotelForm.hotelNumber.length === 4)
          var id = this.hotelForm.hotelNumber;

        this.hotelForm.hotelNumber = data.area.code + id;
      }

      var cityId = this.hotelForm.hotelNumber.slice(0, 4) + "00";
      var areaId = this.hotelForm.hotelNumber.slice(0, 6);
      var area = region[cityId][areaId];
      if (area !== undefined) {

        var geocoder = new TMap.service.Geocoder();
        // 改变地图
        geocoder.getLocation({address: area}).then((result) => {
          this.map.setCenter(result.result.location);
        })
      }
    },

    /** 查询连锁酒店列表 */
    getList() {
      this.loading = true;
      listChainHotel(this.queryParams).then(response => {
        this.chainHotelList = this.handleTree(response.data, "chotelId", "chotelParent");
        this.loading = false;
      });
    },
    /** 转换连锁酒店数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.chotelId,
        label: node.chotelName,
        children: node.children
      };
    },
    /** 查询连锁酒店下拉树结构 */
    getTreeselect() {
      listChainHotel().then(response => {
        this.chainHotelOptions = [];
        const data = {chotelId: 0, chotelName: '顶级节点', children: []};

        data.children = this.handleTree(response.data, "chotelId", "chotelParent");
        this.chainHotelOptions.push(data);
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    cancelForm() {
      this.formOpen = false;
      this.resetHotelForm();
    },
    // 表单重置
    reset() {
      this.form = {
        chotelId: null,
        chotelParent: null,
        chotelName: null,
        chotelType: null,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.form.chotelParent = 0;
      this.open = true;
      this.title = "添加连锁酒店";
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      getChainHotel(row.chotelId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改连锁酒店";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.chotelId != null) {
            updateChainHotel(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addChainHotel(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除连锁酒店编号为"' + row.chotelId + '"的数据项？').then(function () {
        return delChainHotel(row.chotelId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
  },
};
</script>
<style>

</style>

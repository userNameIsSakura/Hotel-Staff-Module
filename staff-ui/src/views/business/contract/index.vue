<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">

      <el-form-item label="合同名" prop="contractName">
        <el-input
          v-model="queryParams.contractName"
          placeholder="请输入合同名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="合同状态" prop="contractState">
        <el-input
          v-model="queryParams.contractState"
          placeholder="请输入合同状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="contractList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="合同名" align="center" prop="contractName" />
      <el-table-column label="签约时间" align="center" prop="contractSign" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.contractSign, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="生效时间" align="center" prop="contractEffect" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.contractEffect, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="到期时间" align="center" prop="contractInvalid" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.contractInvalid, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="合同状态" align="center" prop="contractState" />

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="look(scope.row)"
          >查看</el-button>

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

  </div>
</template>

<script>
import { listContract, getContract, delContract, addContract, updateContract } from "@/api/business/contract";

export default {
  name: "Contract",
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
      // 合同记录表格数据
      contractList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        hotelId: null,
        contractName: null,
        contractFile: null,
        contractSign: null,
        contractEffect: null,
        contractInvalid: null,
        contractState: null,
        modelId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        hotelId: [
          { required: true, message: "酒店id不能为空", trigger: "blur" }
        ],
        contractName: [
          { required: true, message: "合同名不能为空", trigger: "blur" }
        ],
        contractFile: [
          { required: true, message: "合同文件地址不能为空", trigger: "blur" }
        ],
        contractSign: [
          { required: true, message: "签约时间不能为空", trigger: "blur" }
        ],
        contractEffect: [
          { required: true, message: "生效时间不能为空", trigger: "blur" }
        ],
        contractInvalid: [
          { required: true, message: "到期时间不能为空", trigger: "blur" }
        ],
        contractState: [
          { required: true, message: "合同状态不能为空", trigger: "blur" }
        ],
        modelId: [
          { required: true, message: "范本id不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询合同记录列表 */
    getList() {
      this.loading = true;
      listContract(this.queryParams).then(response => {
        this.contractList = response.rows;
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
        contractId: null,
        hotelId: null,
        contractName: null,
        contractFile: null,
        contractSign: null,
        contractEffect: null,
        contractInvalid: null,
        remark: null,
        contractState: null,
        modelId: null
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
    look(row) {
      this.$router.push({path:'/electronicContract',query: {contractId:row.contractId}})
    }


  }
};
</script>

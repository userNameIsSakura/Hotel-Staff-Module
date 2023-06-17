<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="规则名称" prop="ruleName">
        <el-input
          v-model="queryParams.ruleName"
          placeholder="请输入规则名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="有效时间" prop="ruleLifespan">
        <el-date-picker clearable
                        v-model="queryParams.ruleLifespan"
                        type="datetime"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        placeholder="请选择有效时间">
        </el-date-picker>
      </el-form-item>

      <el-form-item label="折扣比例" prop="ruleScale">
        <el-input
          v-model="queryParams.ruleScale"
          placeholder="请输入折扣比例"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="抵扣金额" prop="ruleValue">
        <el-input
          v-model="queryParams.ruleValue"
          placeholder="请输入抵扣金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="金额限制" prop="ruleLimit">
        <el-input
          v-model="queryParams.ruleLimit"
          placeholder="请输入金额限制"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="ruleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="规则名称" align="center" prop="ruleName" />
      <el-table-column label="有效次数" align="center" prop="ruleFrequency" />
      <el-table-column label="有效时间" align="center" prop="ruleLifespan" width="180">
        <template slot-scope="scope">
          <span v-if="scope.row.ruleLifespan !== null">{{ parseTime(scope.row.ruleLifespan, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          <span v-if="scope.row.ruleLifespan === null">永久</span>
        </template>
      </el-table-column>

      <el-table-column label="折扣比例" align="center" prop="ruleScale"/>
      <el-table-column label="抵扣金额" align="center" prop="ruleValue" />

      <el-table-column label="金额限制" align="center" prop="ruleLimit" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="sign(scope.row)"
            v-hasPermi="['system:rule:edit']"
          >订阅</el-button>
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
import {listRule, getRule, delRule, addRule, updateRule, issue, listSignRule, signRule} from "@/api/system/rule";
import {formulate} from "@/api/business/hotelService";

export default {
  name: "Sign",
  data() {
    return {
      // 遮罩层
      loading: true,
      discountType: "折扣",
      ruleFrequency: "有限",
      ruleLifespan: "限时",
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
      // 优惠规则表格数据
      ruleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ruleName: null,
        ruleFrequency: null,
        ruleLifespan: null,
        ruleScope: null,
        ruleScale: null,
        ruleValue: null,
        ruleLimit: null,
        chotelId: null
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询优惠规则列表 */
    getList() {
      this.loading = true;
      listSignRule().then(response => {

        if(response === false) {
          this.$confirm('您所在集团未加入会员系统', '确认', {
            confirmButtonText: '确定',
            type: 'info'
          }).then(() => {
            this.$router.push("/member/memberIndex");
          });
        }

        var list = [];
        list = response.rows;
        for (let i = 0; i < list.length; i++) {
          if (list[i].ruleScale === null)
            list[i].ruleScale = "/";
          else
            list[i].ruleValue = "/";

          if (list[i].ruleFrequency === null)
            list[i].ruleFrequency = "无限次数";
        }
        this.ruleList = list;
        this.total = response.total;
        this.loading = false;
      });
    },

    sign(row) {
      var query = {};
      query.ruleId = row.ruleId;
      this.$modal.confirm('是否确定订阅：' + row.ruleName + "?").then(function () {
        return signRule(query);
      }).then(response => {
        this.$message.success("订阅成功");
      })
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
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
      this.ids = selection.map(item => item.ruleId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
  }
};
</script>

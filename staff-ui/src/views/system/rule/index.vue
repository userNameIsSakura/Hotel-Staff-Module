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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:rule:add']"
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
          v-hasPermi="['system:rule:edit']"
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
          v-hasPermi="['system:rule:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:rule:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

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
            icon="el-icon-user"
            @click="issue(scope.row)"
            v-hasPermi="['system:rule:edit']"
          >发放</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:rule:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:rule:remove']"
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

    <!-- 添加或修改优惠规则对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="规则名称" prop="ruleName">
          <el-input v-model="form.ruleName" placeholder="请输入规则名称" />
        </el-form-item>


        <el-form-item label="有效次数">
          <el-radio-group v-model="ruleFrequency">
            <el-radio
              label="有限"
              value="有限"/>
            <el-radio
              label="无限"
              value="无限"/>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="有效次数" prop="ruleFrequency" v-if="ruleFrequency === '有限'">
          <el-input type="number" v-model="form.ruleFrequency" placeholder="请输入有效次数" />
        </el-form-item>


        <el-form-item label="有效时间">
          <el-radio-group v-model="ruleLifespan">
            <el-radio
              label="限时"
              value="限时"/>
            <el-radio
              label="永久"
              value="永久"/>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="有效时间" prop="ruleLifespan" v-if="ruleLifespan === '限时'">
          <el-date-picker clearable
            v-model="form.ruleLifespan"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择有效时间">
          </el-date-picker>
        </el-form-item>

        <el-form-item label="优惠类型">
          <el-radio-group v-model="discountType">
            <el-radio
              label="折扣"
              value="折扣"/>
            <el-radio
              label="抵价"
              value="抵价"/>
          </el-radio-group>
        </el-form-item>


        <el-form-item label="折扣比例" prop="ruleScale" v-if="discountType === '折扣'">
          <el-input type="number" v-model="form.ruleScale" placeholder="请输入折扣比例" />
        </el-form-item>

        <el-form-item label="抵扣金额" prop="ruleValue" v-if="discountType === '抵价'">
          <el-input type="number" v-model="form.ruleValue" placeholder="请输入抵扣金额"/>
        </el-form-item>

        <el-form-item label="金额限制" prop="ruleLimit">
          <el-input v-model="form.ruleLimit" placeholder="请输入金额限制" />
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
import {listRule, getRule, delRule, addRule, updateRule, issue} from "@/api/system/rule";

export default {
  name: "Rule",
  data() {
    var ruleScaleCheck = (rule, value, callback) => {
      if(value <= 0 || value >= 1)  {
        return callback(new Error("折扣必须是0到1之间的小数"))
      }
      callback();

    };
    var ruleValueCheck = (rule, value, callback) => {
      if(value <= 0)  {
        return callback(new Error("抵扣金额必须大于零"))
      }
      callback();


    };
    var ruleLimit = (rule, value, callback) => {
      if(this.discountType === "抵价" && this.form.ruleValue !== null) {
        if(parseInt(value) <= parseInt(this.form.ruleValue))  {
          return callback(new Error("限制金额不得小于抵扣金额！"))
        }
      }
      callback();

    };

    var ruleFrequencyCheck = (rule, value, callback) => {
      if(value <= 0 || value % 1 !== 0)
        return callback(new Error("有效次数必须是大于零的整数"))
      callback();

    };
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
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        ruleName: [
          { required: true, message: "规则名称不能为空", trigger: "blur" }
        ],
        ruleFrequency: [
          { required: true, message: "有效次数不能为空", trigger: "blur" },
          { validator: ruleFrequencyCheck, trigger: "blur"}
        ],
        ruleLifespan: [
          { required: true, message: "有效时间不能为空", trigger: "blur" }
        ],
        ruleScope: [
          { required: true, message: "生效范围不能为空", trigger: "blur" }
        ],
        ruleScale: [
          { required: true, message: "折扣比例必须是0和1之间的小数", trigger: "blur" },
          { validator: ruleScaleCheck, trigger: "blur"}
        ],
        ruleValue: [
          { required: true, message: "抵扣金额不能为空", trigger: "blur" },
          { validator: ruleValueCheck, trigger: "blur"}
        ],
        ruleLimit: [
          { required: true, message: "金额限制不能为空", trigger: "blur" },
          { validator: ruleLimit, trigger: "blur"}
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    issue(row) {
      var query = {};
      query.ruleId = row.ruleId;
      this.$modal.confirm('是否确定发布优惠券：' + row.ruleName + "?").then(function () {
        return issue(query);
      }).then(response => {
        this.$message.success("发布成功,本次一共发布了" + response.num + "张优惠券");
      })
    },
    /** 查询优惠规则列表 */

    getList() {
      this.loading = true;
      listRule(this.queryParams).then(response => {
        var list = [];
        list = response.rows;
        for (let i = 0; i < list.length; i++) {
          if(list[i].ruleScale === null)
            list[i].ruleScale = "/";
          else
            list[i].ruleValue = "/";

          if(list[i].ruleFrequency === null)
            list[i].ruleFrequency = "无限次数";
        }
        this.ruleList = list;
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
        ruleId: null,
        ruleName: null,
        ruleFrequency: null,
        ruleLifespan: null,
        ruleScope: null,
        ruleScale: null,
        ruleValue: null,
        ruleLimit: null,
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
      this.ids = selection.map(item => item.ruleId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加优惠规则";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const ruleId = row.ruleId || this.ids
      getRule(ruleId).then(response => {
        this.form = response.data;
        if(response.data.ruleValue === null) {
          this.discountType = "折扣";
        }else{
          this.discountType = "抵价";
        }

        if(response.data.ruleFrequency === null) {
          this.ruleFrequency = "无限";
        }
        if(response.data.ruleLifespan === null) {
          this.ruleLifespan = "永久";
        }

        this.open = true;
        this.title = "修改优惠规则";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {

          if(this.discountType === "折扣")
            this.form.ruleValue = null;
          else
            this.form.ruleScale = null;

          if(this.ruleFrequency === "无限")
            this.form.ruleFrequency = null;
          if(this.ruleLifespan === "永久")
            this.form.ruleLifespan = null;

          if (this.form.ruleId != null) {
            updateRule(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRule(this.form).then(response => {
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
      const ruleIds = row.ruleId || this.ids;
      this.$modal.confirm('是否确认删除优惠规则编号为"' + ruleIds + '"的数据项？').then(function() {
        return delRule(ruleIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/rule/export', {
        ...this.queryParams
      }, `rule_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

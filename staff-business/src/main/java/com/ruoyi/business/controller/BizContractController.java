package com.ruoyi.business.controller;

import com.ruoyi.business.domain.BizContract;
import com.ruoyi.business.service.IBaseModelService;
import com.ruoyi.business.service.IBizContractService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.ServerException;
import java.util.List;

/**
 * 合同记录Controller
 *
 * @author ruoyi
 * @date 2022-12-07
 */
@RestController
@RequestMapping("/business/contract")
public class BizContractController extends BaseController
{
    @Autowired
    private IBizContractService bizContractService;
    @Autowired
    private IBaseModelService baseModelService;

    /**
     * 查询合同记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BizContract bizContract,HttpServletResponse response)
    {
        startPage();
        Long hotelId = SecurityUtils.getHotelId();
        bizContract.setHotelId(hotelId);
        List<BizContract> list = bizContractService.selectBizContractList(bizContract);
        return getDataTable(list);
    }

    /**
     * 导出合同记录列表
     */
/*    @Log(title = "合同记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizContract bizContract)
    {
        List<BizContract> list = bizContractService.selectBizContractList(bizContract);
        ExcelUtil<BizContract> util = new ExcelUtil<BizContract>(BizContract.class);
        util.exportExcel(response, list, "合同记录数据");
    }*/

    /**
     *
     * 查询历史合同
     *
     * */
    @PostMapping("/historyContract")
    public void historyContract(@RequestBody Long id,HttpServletResponse response) throws IOException {
        BizContract bizContract = bizContractService.selectBizContractByContractId(id);
        if(bizContract == null) {
            response.setStatus(404);
            response.setHeader("content-type", "text/html;charset=UTF-8");//通过设置响应头控制浏览器以UTF-8的编码显示数据
            return;
        }

        try {
            System.out.println(bizContract.getContractFile());
            File file = new File(bizContract.getContractFile());
            InputStream in = new FileInputStream(file);
            ServletOutputStream outputStream = response.getOutputStream();
            //创建存放文件内容的数组
            byte[] buff =new byte[in.available()];
            in.read(buff);
            outputStream.write(buff);
            //强制将缓存区的数据进行输出
            outputStream.println();
            //关流
            outputStream.close();
            in.close();
        }catch (Exception ex) {
            throw new ServerException("文件不存在");
        }

    }



}

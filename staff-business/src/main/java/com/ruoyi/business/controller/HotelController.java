package com.ruoyi.business.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.ruoyi.business.domain.BaseChainHotel;
import com.ruoyi.business.domain.BaseHotel;
import com.ruoyi.business.domain.BaseModel;
import com.ruoyi.business.domain.BizContract;
import com.ruoyi.business.service.IBaseChainHotelService;
import com.ruoyi.business.service.IBaseHotelService;
import com.ruoyi.business.service.IBaseModelService;
import com.ruoyi.business.service.IBizContractService;
import com.ruoyi.business.utils.TestRenderListener;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping(("/hotel"))
public class HotelController {

    @Autowired
    private IBaseModelService baseModelService;
    @Autowired
    private IBizContractService bizContractService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IBaseChainHotelService hotelService;


    /*默认保存位置*/
    @Value("${pdf.path}")
    private String pdfPath;
    /*甲方公司名*/
    @Value("${pdf.partyA.name}")
    private String partAName;
    /*合同有效期*/
    @Value("${pdf.periodOfValidity}")
    private Long periodOfValidity;
    @Value("${ruoyi.profile}")
    private String pathPrefix;

    @PostMapping("/pdf")
    public void getPDF(@RequestBody int id, HttpServletResponse response) throws IOException {

        BaseModel baseModel = baseModelService.selectBaseModelByModelId((long) id);
        if(baseModel == null) {
            response.setStatus(404);
            response.setHeader("content-type", "text/html;charset=UTF-8");//通过设置响应头控制浏览器以UTF-8的编码显示数据
            return;
        }

        final String modelFile = baseModel.getModelFile();
        final String fileName = modelFile.replaceFirst("/profile", "");

        File file = new File(pathPrefix + fileName);
        InputStream in = new FileInputStream(file);
        ServletOutputStream outputStream = response.getOutputStream();
        //创建存放文件内容的数组
        byte[] buff =new byte[1024];
        //所读取的内容使用n来接收
        int n;
        //当没有读取完时,继续读取,循环
        while((n=in.read(buff))!=-1){
            //将字节数组的数据全部写入到输出流中
            outputStream.write(buff,0,n);
        }
        //强制将缓存区的数据进行输出
        outputStream.println();
        //关流
        outputStream.close();
        in.close();
    }

    @PostMapping("/formulate")
    public AjaxResult formulate(@RequestBody int id, HttpServletResponse response) throws Exception {

        AjaxResult success = AjaxResult.success();

/*        System.out.println(String.valueOf(SecurityUtils.getUserId()));
        *//*如果已经签订合同且在有效期则拒绝签订*//*
        String s = HttpUtils.sendPost(staffPath + "getHotelId", "id="+ SecurityUtils.getUserId());
        if(s.equals("0")) {
            AjaxResult error = AjaxResult.error("管理员酒店信息查询异常");
            return error;
        }*/
        SysUser sysUser = sysUserService.selectUserById(SecurityUtils.getUserId());
        Long hotelId = sysUser.getHotelId();

        final BaseChainHotel hotel = hotelService.selectBaseChainHotelByChotelId(hotelId);
        final String hotelName = hotel.getChotelName();

        BizContract bizContract = new BizContract();
        bizContract.setHotelId(hotelId);
        bizContract.setModelId(Long.valueOf(id));
        List<BizContract> bizContracts = bizContractService.selectBizContractList(bizContract);

        long now = System.currentTimeMillis();
        long tomorrow = now + 1000*60*60*24;
        if(bizContracts.size() != 0) {
            for (BizContract contract : bizContracts) {
                Date contractInvalid = contract.getContractInvalid();
                long time = contractInvalid.getTime();
                /*有合同在有效期内*/
                if(tomorrow <= time) {
                    AjaxResult error = AjaxResult.error("您所在酒店已签订该合同");
                    return error;
                }
            }
        }


        BaseModel baseModel = baseModelService.selectBaseModelByModelId((long) id);
        if(baseModel == null) {
            AjaxResult error = AjaxResult.error("范本不存在或已删除");
            return error;
        }

        //设置中文
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);

        Document document = new Document();
        PdfCopy copy = null;
        OutputStream os = null;

        try {
            /*生效时间*/
            Date date = new Date(tomorrow);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),0,0,0);
            /*失效时间*/
            Date failureTime = new Date(tomorrow + periodOfValidity);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(failureTime);
            calendar1.set(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),calendar1.get(Calendar.DAY_OF_MONTH),0,0,0);

            /*ITextPDF*/
            File folder = new File(pdfPath+hotelName);

            if (!folder.exists() && !folder.isDirectory()) {
                folder.mkdirs();
            }

            String fileName = getFileName(hotelName,baseModel.getModelName(),calendar,calendar1);
            String filePath = pdfPath + hotelName + "\\" + fileName;
            os = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
            PdfWriter writer = PdfWriter.getInstance(document, os);
            document.open();
            PdfContentByte cb = writer.getDirectContent();

            final String modelFile = baseModel.getModelFile();
            final String path = modelFile.replaceFirst("/profile", "");
            File file = new File(pathPrefix + path);

            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            PdfReader pdfReader = new PdfReader(bufferedInputStream);
            int sum = pdfReader.getNumberOfPages();

            //创建pdf解析类
            PdfReaderContentParser parser = new PdfReaderContentParser(pdfReader);
            TestRenderListener listener = new TestRenderListener();

            //是否需要加页
            AtomicBoolean addPage = new AtomicBoolean(false);

            // 解析PDF，并处理里面的文字
            parser.processContent(sum, listener);
            // 获取文字的矩形边框
/*            List<Rectangle2D.Float> rectText = listener.rectText;
            List<String> textList = listener.textList;*/
            List<Float> listY = listener.listY;
            listY.forEach(y -> {
                if(y < 160) {
                    addPage.set(true);
                }
            });


            List<PdfImportedPage> pdfImportedPages = new ArrayList<>();
            for (int i = 0; i < sum; i++) {
                PdfImportedPage page = writer.getImportedPage(pdfReader, i+1);
                pdfImportedPages.add(page);
            }

            pdfImportedPages.forEach(page -> {
                document.newPage();
                cb.addTemplate(page,0,0);
            });


            if(addPage.get()) {
                //需要加页
                document.newPage();
                cb.beginText();
                cb.setFontAndSize(bfChinese,10);

                cb.setTextMatrix(400,750);
                cb.showText("甲方: " + partAName);

                cb.setTextMatrix(400,730);
                cb.showText("乙方: " + hotelName);

                cb.setTextMatrix(400,710);
                cb.showText("生效时间: " + DateUtils.getDateByCalender(calendar));

                cb.setTextMatrix(400,690);
                cb.showText("失效时间: " + DateUtils.getDateByCalender(calendar1));

                cb.endText();
            }else {
                //无需加页
                cb.beginText();
                cb.setFontAndSize(bfChinese,10);

                cb.setTextMatrix(400,160);
                cb.showText("甲方: " + partAName);

                cb.setTextMatrix(400,140);
                cb.showText("乙方: " + hotelName);

                cb.setTextMatrix(400,120);
                cb.showText("生效时间: " + DateUtils.getDateByCalender(calendar));

                cb.setTextMatrix(400,100);
                cb.showText("失效时间: " + DateUtils.getDateByCalender(calendar1));

                cb.endText();
            }



            /* 维护合同表 */
            bizContract = new BizContract();
            bizContract.setContractName(fileName);
            bizContract.setContractSign(new Date(now));
            bizContract.setContractEffect(calendar.getTime());
            bizContract.setContractInvalid(calendar1.getTime());
            bizContract.setContractState("已签字");
            bizContract.setModelId(baseModel.getModelId());
            bizContract.setHotelId(hotelId);
            bizContract.setContractFile(filePath);
            bizContractService.insertBizContract(bizContract);

        }catch (Exception e) {
            e.printStackTrace();
            AjaxResult error = AjaxResult.error("文件签订失败");
            return error;

        }finally {
            if (copy != null) {
                try {
                    copy.close();
                } catch (Exception ex) {
                    /* ignore */
                    AjaxResult error = AjaxResult.error("生成PDF异常");
                    return error;
                }
            }
            if (document != null) {
                try {
                    document.close();
                } catch (Exception ex) {
                    /* ignore */
                    AjaxResult error = AjaxResult.error("生成PDF异常");
                    return error;
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception ex) {
                    /* ignore */
                    AjaxResult error = AjaxResult.error("生成PDF异常");
                    return error;
                }
            }
        }
        return success;
    }

    private String getFileName(String hotelName, String modelName, Calendar start, Calendar end) {

        return hotelName+"-"+modelName+"-"+DateUtils.getDateByCalender(start) + "-" + DateUtils.getDateByCalender(end)+".pdf";

    }
}




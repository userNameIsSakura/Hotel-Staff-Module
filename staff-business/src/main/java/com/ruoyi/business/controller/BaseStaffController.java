package com.ruoyi.business.controller;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.business.domain.BaseHotel;
import com.ruoyi.business.domain.BaseRole;
import com.ruoyi.business.domain.StaffRoleRelationships;
import com.ruoyi.business.mapper.BaseStaffMapper;
import com.ruoyi.business.service.IBaseHotelService;
import com.ruoyi.business.service.IBaseRoleService;
import com.ruoyi.business.utils.ServiceNameUtil;
import com.ruoyi.common.core.domain.model.StaffUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.TokenService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.business.domain.BaseStaff;
import com.ruoyi.business.service.IBaseStaffService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 员工信息Controller
 *
 * @author ruoyi
 * @date 2022-11-10
 */
@RestController
@RequestMapping("/business/staff")
public class BaseStaffController extends BaseController
{
    @Autowired
    private IBaseStaffService baseStaffService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IBaseRoleService baseRoleService;
    @Autowired
    private BaseStaffMapper baseStaffMapper;
    @Autowired
    private IBaseHotelService hotelService;
    @Autowired
    private IBaseHotelService baseHotelService;

//    private static AtomicInteger clientId = new AtomicInteger(0);
    private static HashMap<String,AtomicInteger> clientid = new HashMap<>();

    /**
     * 查询员工信息列表
     *
     * @param baseStaff 员工
     * @return {@link TableDataInfo}
     */
    @PreAuthorize("@ss.hasPermi('business:staff:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseStaff baseStaff)
    {
        startPage();

        baseStaff.setHotelId(baseHotelService.selectBaseHotelByChotelId(baseStaff.getHotelId()).getHotelId());

        List<BaseStaff> list = baseStaffService.selectBaseStaffList(baseStaff);
        return getDataTable(list);
    }

    /**
     * 登录
     *
     * @param map 地图
     * @return {@link HashMap}
     */
    @PostMapping("/login")
    public HashMap login(@RequestBody HashMap map)
    {
        String phone = (String) map.get("phone");
        String password = (String) map.get("password");

        /* 账号密码验证 */
        BaseStaff baseStaff = new BaseStaff();
        baseStaff.setStaffPhone(phone);
        List<BaseStaff> baseStaffs = baseStaffService.selectBaseStaffList(baseStaff);

        if(baseStaffs.size() == 0) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("msg","账号不存在");
            return hashMap;
        }

        BaseStaff staff = baseStaffs.get(0);
        if (SecurityUtils.matchesPassword(password,staff.getStaffPassword())) {
            final String clientTopic = getClientTopic(staff.getStaffPhone());
            HashMap<String, String> hashMap = new HashMap<>();
            String token = tokenService.createStaffToken(staff.getStaffId(),staff.getStaffPhone(),staff.getHotelId(),clientTopic);
            hashMap.put("msg","登录成功");
            hashMap.put("token",token);
            hashMap.put("hotelId",hotelService.selectBaseHotelByHotelId(staff.getHotelId()).getHotelNumber());


            if(ServiceNameUtil.Service_Hotel_Id.equals(-1L)) {
                final BaseHotel hotel = new BaseHotel();
                hotel.setHotelName(ServiceNameUtil.SERVICE_HOTEL_NAME);
                final List<BaseHotel> baseHotels = baseHotelService.selectBaseHotelList(hotel);
                if(baseHotels.size() == 0) {
                    return AjaxResult.error("系统异常，未查询到内部模块信息");
                }
                if(baseHotels.size() > 1) {
                    return AjaxResult.error("系统异常，有多条命名恒和的数据");
                }
                ServiceNameUtil.Service_Hotel_Id = baseHotels.get(0).getHotelId();
            }

            if(staff.getHotelId().equals(ServiceNameUtil.Service_Hotel_Id)) {
                switch (staff.getStaffName()) {
                    // TODO: 2023/6/14 补齐其他服务
                    case ServiceNameUtil.FILE_SERVICE: {
                        hashMap.put("topic",ServiceNameUtil.FILE_SERVICE_TOPIC);
                        break;
                    }
                    default: {
                        hashMap.put("topic",clientTopic);
                    }
                }
            }else {
                hashMap.put("topic",clientTopic);
            }

            return hashMap;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("msg","密码错误");
        return hashMap;
    }

    public static String getClientTopic(String phone) {
        if(!clientid.containsKey(phone))
            clientid.put(phone,new AtomicInteger(0));
        int andIncrement = clientid.get(phone).getAndIncrement();
        String format = String.format("%03d", andIncrement);
        return "topic_zzj_" + phone + format;
    }

    /**
     * 心跳
     *
     * @param request 请求
     * @return boolean
     */
    @GetMapping("/heart")
    public boolean heart(HttpServletRequest request) {

        StaffUser staffUser = tokenService.getStaffUser(request);
        if(staffUser == null) {
            return false;
        }
        tokenService.verifyStaffToken(staffUser);
        return true;
    }

    @PostMapping("/validate")
    public AjaxResult heart(@RequestBody HashMap<String,String> map,HttpServletRequest request) {

        if(!heart(request))
            return AjaxResult.error("无权限");

        final String token = map.get("token");
        if(StringUtils.isEmpty(token))
            return AjaxResult.error("未携带验证用Token");
        StaffUser staffUser = tokenService.getStaffUserByToken(token);
        if(staffUser == null) {
            return AjaxResult.error("Token无效或已过期");
        }
        tokenService.verifyStaffToken(staffUser);
        return AjaxResult.success(hotelService.selectBaseHotelByHotelId(staffUser.getHotelId()));
    }

    /**
     * 导出员工信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:staff:export')")
    @Log(title = "员工信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseStaff baseStaff)
    {
        baseStaff.setHotelId(baseHotelService.selectBaseHotelByChotelId(baseStaff.getHotelId()).getHotelId());
        List<BaseStaff> list = baseStaffService.selectBaseStaffList(baseStaff);
        ExcelUtil<BaseStaff> util = new ExcelUtil<BaseStaff>(BaseStaff.class);
        util.exportExcel(response, list, "员工信息数据");
    }

    /**
     * 获取员工信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:staff:query')")
    @GetMapping(value = {"/{hotelId}/","/{hotelId}/{staffId}"})
    public AjaxResult getInfo(@PathVariable(value = "hotelId",required = false) Long hotelId,@PathVariable(value = "staffId",required = false) Long staffId)
    {
        AjaxResult success = AjaxResult.success(baseStaffService.selectBaseStaffByStaffId(staffId));

        BaseRole baseRole = new BaseRole();
        baseRole.setHotelId(baseHotelService.selectBaseHotelByChotelId(hotelId).getHotelId());
        List<BaseRole> baseRoles = baseRoleService.selectBaseRoleList(baseRole);
        success.put("roleList",baseRoles);

        List<Long> roles = baseRoleService.selectBaseRoleByStaffId(staffId).stream().map(BaseRole::getRoleId).collect(Collectors.toList());
        success.put("roles",roles);

        return success;
    }

    /**
     * 新增员工信息
     */
    @PreAuthorize("@ss.hasPermi('business:staff:add')")
    @Log(title = "员工信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseStaff baseStaff)
    {

        List<BaseStaff> list = baseStaffMapper.checkStaffPhone(baseStaff.getStaffPhone());
        if(list.size() != 0) {
            return AjaxResult.error("新增用户'" + baseStaff.getStaffName() + "'失败，手机号码已存在");
        }
        baseStaff.setHotelId(baseHotelService.selectBaseHotelByChotelId(baseStaff.getHotelId()).getHotelId());
        baseStaff.setStaffPassword(SecurityUtils.encryptPassword(baseStaff.getStaffPassword()));
        return toAjax(baseStaffService.insertBaseStaff(baseStaff));
    }

    /**
     * 修改员工信息
     */
    @PreAuthorize("@ss.hasPermi('business:staff:edit')")
    @Log(title = "员工信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseStaff baseStaff)
    {
        baseStaff.setHotelId(baseHotelService.selectBaseHotelByChotelId(baseStaff.getHotelId()).getHotelId());
        List<BaseStaff> list = baseStaffMapper.checkStaffPhone(baseStaff.getStaffPhone());

        if(list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                if(!list.get(i).getStaffId().equals(baseStaff.getStaffId()))
                {
                    return AjaxResult.error("修改用户'" + baseStaff.getStaffName() + "'失败，手机号码已存在");
                }
            }
        }

        return toAjax(baseStaffService.updateBaseStaff(baseStaff));
    }

    /**
     * 重置员工密码
     * */
    @PreAuthorize("@ss.hasPermi('business:staff:edit')")
    @PutMapping("/reset")
    public AjaxResult reset(@RequestBody BaseStaff staff) {
        staff.setStaffPassword(SecurityUtils.encryptPassword(staff.getStaffPassword()));
        return toAjax(baseStaffMapper.updateBaseStaffPassword(staff));

    }

    /**
     * 删除员工信息
     */
    @PreAuthorize("@ss.hasPermi('business:staff:remove')")
    @Log(title = "员工信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{staffIds}")
    public AjaxResult remove(@PathVariable Long[] staffIds)
    {
        return toAjax(baseStaffService.deleteBaseStaffByStaffIds(staffIds));
    }
}

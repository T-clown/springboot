package com.springboot.controller;

import cn.hutool.poi.excel.ExcelReader;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.springboot.common.entity.Result;
import com.springboot.common.utils.ResultUtil;
import com.springboot.dao.dto.UserDTO;
import com.springboot.domain.entity.User;
import com.springboot.domain.entity.UserQueryRequest;
import com.springboot.domain.entity.excel.DownloadData;
import com.springboot.domain.entity.excel.UploadData;
import com.springboot.service.repository.UserRepository;
import com.springboot.utils.BeanCopyUtils;
import com.springboot.utils.excel.easyexcel.EasyExcelSheet;
import com.springboot.utils.excel.easyexcel.ExcelUtil;
import com.springboot.utils.PPTToImageUtil;
import com.springboot.utils.excel.poi.Column;
import com.springboot.utils.excel.poi.DownloadExcelSheet;
import com.springboot.utils.excel.poi.ExcelParser;
import com.springboot.utils.excel.poi.ExcelSheet;
import com.springboot.utils.excel.poi.TestData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Excel导入导出")
@Slf4j
@RestController
@RequestMapping("/excel")
public class ExcelController {


    @Autowired
    private UserRepository userRepository;

    /**
     * @Desc 批量导出
     **/
    @Operation(summary = "批量导出用户(固定表头-单个sheet)）")
    @PostMapping(value = "/export/user")
    public void exportUser(@RequestBody UserQueryRequest request, HttpServletResponse response) {
        List<UserDTO> userDTOS = userRepository.list(request);
        List<DownloadData> data = userDTOS.stream().map(x -> BeanCopyUtils.copyProperties(x, DownloadData.class)).collect(Collectors.toList());
        ExcelUtil.exportExcel(response, DownloadData.class, data, "固定表头excel");
    }

    @Operation(summary = "批量导出用户(动态表头-多sheet)）")
    @PostMapping(value = "/export/user2")
    public void exportUser2(HttpServletResponse response) throws IOException {
        ExcelUtil.exportExcel2(response, sheets(), "测试");
    }


    private List<EasyExcelSheet> sheets() {
        List<Column> columns = Arrays.asList(new Column("key1", "列1"), new Column("key2", "列2"), new Column("key3", "列3"));
        List<String> header1 = Arrays.asList("标题1", "标题2", "标题3");
        List<String> header2 = Arrays.asList("标题3", "标题4", "标题5");

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("key1", 1);
        data.put("key3", new Date());
        data.put("key2", "你好");

        EasyExcelSheet sheet1 = EasyExcelSheet.builder()
                .sheetName("sheet1")
                .columns(columns)
                .excludeColumns(Arrays.asList("key3"))
                .headers(Arrays.asList(header1, header2))
                .data(Arrays.asList(data))
                .build();

        EasyExcelSheet sheet2 = EasyExcelSheet.builder()
                .sheetName("sheet2")
                .columns(columns)
                .excludeColumns(Arrays.asList("key1"))
                .headers(Arrays.asList(header1, header2))
                .data(Arrays.asList(data))
                .build();

        return Arrays.asList(sheet1, sheet2);
    }

    /**
     * 文件上传
     */
    @PostMapping("/upload/user")
    public Result<Void> upload(MultipartFile file) {
        ExcelUtil.upload(file, UploadData.class, this::convert, t -> userRepository.addUser(t));
        return ResultUtil.success();
    }

    @PostMapping("/upload/excel/poi")
    public Result<Void> uploadExcel2(MultipartFile file) throws IOException {
        ExcelParser<TestData> excelParser = new ExcelParser<>(TestData.class);
        List<ExcelSheet<TestData>> parse = excelParser.parse(file.getInputStream());
        parse.forEach(x->log.info(String.valueOf(x.getData().size())));
        return ResultUtil.success();
    }


    @PostMapping("/upload/excel")
    public Result<Void> uploadExcel(MultipartFile file) {
        try {
            ExcelReader excelReader = new ExcelReader(file.getInputStream(), 0);
            //解决导入的Excel中的第一行类型和实体类不一样
            excelReader.addHeaderAlias("create_time", "createTime");
            ;

            excelReader.addHeaderAlias("create_date", "createDate");
            //直接把Excel中的内容映射到实体类中
            List<User> tests = excelReader.read(0, 0, User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultUtil.success();
    }

    /**
     * 文件上传
     */
    @PostMapping("/upload/ppt")
    public Result<Void> pptToImage(MultipartFile file) throws IOException {
        String path = "/Users/hrtps/tmp";
        File file1 = new File("/Users/hrtps/Desktop/测试.pdf");
        PPTToImageUtil.convertPdf2Png(file1, path);
        return ResultUtil.success();
    }

    private UserDTO convert(UploadData uploadData) {

        return BeanCopyUtils.copyProperties(uploadData, UserDTO.class);
    }

    /**
     * 反序列化带泛型的对象集合
     * @param args
     */
    public static void main(String[] args) {
        String data="[{\"columns\":[{\"key\":\"account\",\"name\":\"账户\",\"width\":20},{\"key\":\"name\",\"name\":\"姓名\",\"width\":20},{\"key\":\"errorMessage\",\"name\":\"错误信息\",\"width\":20}],\"data\":[{\"name\":\"许家印\",\"account\":\"q01\"},{\"name\":\"李书福\",\"account\":\"q02\"},{\"name\":\"郭广昌\",\"account\":\"q03\"},{\"name\":\"俞敏洪\",\"account\":\"q04\"},{\"name\":\"宗亲后\",\"account\":\"q07\"},{\"name\":\"张1\",\"account\":\"sq001\"},{\"name\":\"张2\",\"account\":\"sq002\"},{\"name\":\"张3\",\"account\":\"sq003\"},{\"name\":\"张4\",\"account\":\"sq004\"},{\"name\":\"张5\",\"account\":\"sq005\"},{\"name\":\"张6\",\"account\":\"sq006\"},{\"name\":\"张7\",\"account\":\"sq007\"},{\"name\":\"张8\",\"account\":\"sq008\"},{\"name\":\"张9\",\"account\":\"sq009\"},{\"name\":\"张10\",\"account\":\"sq010\"},{\"name\":\"张11\",\"account\":\"sq011\"},{\"name\":\"张12\",\"account\":\"sq012\"},{\"name\":\"张13\",\"account\":\"sq013\"},{\"name\":\"张14\",\"account\":\"sq014\"},{\"name\":\"张15\",\"account\":\"sq015\"},{\"name\":\"张16\",\"account\":\"sq016\"},{\"name\":\"张17\",\"account\":\"sq017\"},{\"name\":\"张18\",\"account\":\"sq018\"},{\"name\":\"张19\",\"account\":\"sq019\"},{\"name\":\"张20\",\"account\":\"sq020\"},{\"name\":\"张21\",\"account\":\"sq021\"},{\"name\":\"张22\",\"account\":\"sq022\"},{\"name\":\"张23\",\"account\":\"sq023\"},{\"name\":\"张24\",\"account\":\"sq024\"},{\"name\":\"张25\",\"account\":\"sq025\"},{\"name\":\"张26\",\"account\":\"sq026\"},{\"name\":\"张27\",\"account\":\"sq027\"},{\"name\":\"张28\",\"account\":\"sq028\"},{\"name\":\"张29\",\"account\":\"sq029\"},{\"name\":\"张30\",\"account\":\"sq030\"},{\"name\":\"张31\",\"account\":\"sq031\"},{\"name\":\"张32\",\"account\":\"sq032\"},{\"name\":\"张33\",\"account\":\"sq033\"},{\"name\":\"张34\",\"account\":\"sq034\"},{\"name\":\"张35\",\"account\":\"sq035\"},{\"name\":\"张36\",\"account\":\"sq036\"},{\"name\":\"张37\",\"account\":\"sq037\"},{\"name\":\"张38\",\"account\":\"sq038\"},{\"name\":\"张39\",\"account\":\"sq039\"},{\"name\":\"张40\",\"account\":\"sq040\"},{\"name\":\"张41\",\"account\":\"sq041\"},{\"name\":\"张42\",\"account\":\"sq042\"},{\"name\":\"张43\",\"account\":\"sq043\"},{\"name\":\"张44\",\"account\":\"sq044\"},{\"name\":\"张45\",\"account\":\"sq045\"},{\"name\":\"张46\",\"account\":\"sq046\"},{\"name\":\"张47\",\"account\":\"sq047\"},{\"name\":\"张48\",\"account\":\"sq048\"},{\"name\":\"张49\",\"account\":\"sq049\"},{\"name\":\"张50\",\"account\":\"sq050\"},{\"name\":\"张51\",\"account\":\"sq051\"},{\"name\":\"张52\",\"account\":\"sq052\"},{\"name\":\"张53\",\"account\":\"sq053\"},{\"name\":\"张54\",\"account\":\"sq054\"},{\"name\":\"张55\",\"account\":\"sq055\"},{\"name\":\"张56\",\"account\":\"sq056\"},{\"name\":\"张57\",\"account\":\"sq057\"},{\"name\":\"张58\",\"account\":\"sq058\"},{\"name\":\"张59\",\"account\":\"sq059\"},{\"name\":\"张60\",\"account\":\"sq060\"},{\"name\":\"张61\",\"account\":\"sq061\"},{\"name\":\"张62\",\"account\":\"sq062\"},{\"name\":\"张63\",\"account\":\"sq063\"},{\"name\":\"张64\",\"account\":\"sq064\"},{\"name\":\"张65\",\"account\":\"sq065\"},{\"name\":\"张66\",\"account\":\"sq066\"},{\"name\":\"张67\",\"account\":\"sq067\"},{\"name\":\"张68\",\"account\":\"sq068\"},{\"name\":\"张69\",\"account\":\"sq069\"},{\"name\":\"张70\",\"account\":\"sq070\"},{\"name\":\"张71\",\"account\":\"sq071\"},{\"name\":\"张72\",\"account\":\"sq072\"},{\"name\":\"张73\",\"account\":\"sq073\"},{\"name\":\"张74\",\"account\":\"sq074\"},{\"name\":\"张75\",\"account\":\"sq075\"},{\"name\":\"张76\",\"account\":\"sq076\"},{\"name\":\"张77\",\"account\":\"sq077\"},{\"name\":\"张78\",\"account\":\"sq078\"},{\"name\":\"张79\",\"account\":\"sq079\"},{\"name\":\"张80\",\"account\":\"sq080\"},{\"name\":\"张81\",\"account\":\"sq081\"},{\"name\":\"张82\",\"account\":\"sq082\"},{\"name\":\"张83\",\"account\":\"sq083\"},{\"name\":\"张84\",\"account\":\"sq084\"},{\"name\":\"张85\",\"account\":\"sq085\"},{\"name\":\"张86\",\"account\":\"sq086\"},{\"name\":\"张87\",\"account\":\"sq087\"},{\"name\":\"张88\",\"account\":\"sq088\"},{\"name\":\"张89\",\"account\":\"sq089\"},{\"name\":\"张90\",\"account\":\"sq090\"},{\"name\":\"张91\",\"account\":\"sq091\"},{\"name\":\"张92\",\"account\":\"sq092\"},{\"name\":\"张93\",\"account\":\"sq093\"},{\"name\":\"张94\",\"account\":\"sq094\"},{\"name\":\"张95\",\"account\":\"sq095\"},{\"name\":\"张96\",\"account\":\"sq096\"},{\"name\":\"xu901\",\"account\":\"xu9090\"}],\"sheetName\":\"1、批量上传001（2.0冒烟测试）\"}]";

        List<DownloadExcelSheet<Map<String, Object>>> excelSheets = JSON.parseObject(data, new TypeReference<>() {
        });

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<DownloadExcelSheet<Map<String, Object>>> list3 = mapper.readValue(data, new com.fasterxml.jackson.core.type.TypeReference<>() {
            });

            System.out.println();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }

}

package com.atguigu.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.bean.Msg;
import com.atguigu.crud.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
public class EmployeeController {
	@Autowired
	EmployeeService employeeService;
	
	@RequestMapping(value="/emp/{ids}",method=RequestMethod.DELETE)
	@ResponseBody
	public Msg deleteEmp(@PathVariable("ids")String ids){
		//����ɾ��
		if(ids.contains("-")){
			List<Integer> del_ids = new ArrayList<>();
			String[] str_ids = ids.split("-");
			for(String string :str_ids){
				del_ids.add(Integer.parseInt(string));
			}
			employeeService.deleteBatch(del_ids);
		}else{
			//����ɾ��
			Integer id = Integer.parseInt(ids);
			employeeService.deleteEmp(id);
		}
		
		return Msg.success();
	}
	
	
	
	/**
	 * ���ֱ�ӷ���ajax=PUT��ʽ������
	 * ��������������,����employee�����޷���װ
	 * ԭ��:1.tomcat���������е����ݷ�װ��map
	 * 2.request.getParameter("empName")��map��ȡֵ
	 * 3.SpringMVC��װpojo�����ÿ�����Ե�ֵrequest.getParameter()
	 * ajax����PUT������������:tomcat�����װPUT�������е�����Ϊmap,ֻ��POST���װ
	 * 
	 * Ա������
	 * @param employee
	 * @return
	 */
	@RequestMapping(value="/emp/{empId}",method=RequestMethod.PUT)
	@ResponseBody
	public Msg updateEmp(Employee employee){
		employeeService.updateEmp(employee);
		return Msg.success();
	}
	
	/**
	 * ����id��ѯԱ��
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/emp/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@PathVariable("id")Integer id){
		
		Employee employee = employeeService.getEmp(id);
		return Msg.success().add("emp", employee);
	}
	
	
	
	/**
	 * ����û����Ƿ����
	 * 
	 * @param empName
	 * @return
	 */
	@RequestMapping("/checkuser")
	@ResponseBody
	public Msg checkuser(@RequestParam("empName") String empName) {
		// ���ж��û����Ƿ�Ϸ����ʽ
		String regx = "(^[a-z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5}$)";
		if (!empName.matches(regx)) {
			return Msg.fail().add("va_msg", "�û���������2-5λ���Ļ���6-16λӢ�ĺ��������");
		}

		// ���ݿ��û����ظ�У��
		boolean b = employeeService.checkUser(empName);
		if (b) {
			return Msg.success();
		} else {
			return Msg.fail().add("va_msg", "�û���������");
		}
	}

	/**
	 * ����Ա�� JSR303У��
	 * Employee bean��ע�⼴��
	 * @param employee
	 * @return
	 */
	@RequestMapping(value = "/emp", method = RequestMethod.POST)
	@ResponseBody
	public Msg saveEmp(@Valid Employee employee, BindingResult result) {
		if (result.hasErrors()) {
			Map<String, Object> map = new HashMap<>();
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError fieldError : errors) {
//				System.out.println("�����ֶ���:"+fieldError.getField());
//				System.out.println("������Ϣ:"+fieldError.getDefaultMessage());
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return Msg.fail().add("errorFields", map);
		} else {
			employeeService.saveEmp(employee);
			return Msg.success();
		}
	}

	/**
	 * ��Ҫjackson��֧��
	 * 
	 * @param pn
	 * @return
	 */
	@RequestMapping("/emps")
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "1") Integer pn) {
		PageHelper.startPage(pn, 10);
		// ��ѯ��Ϊ��ҳ��ѯ
		List<Employee> emps = employeeService.getAll();
		// PageInfo��֤��ѯ����,��PageInfo����ҳ��,��װ����ϸ��ҳ��Ϣ
		PageInfo pageInfo = new PageInfo(emps, 5);
		return Msg.success().add("pageInfo", pageInfo);
	}

	/**
	 * ��ѯԱ������(��ҳ��ѯ)
	 */
	// @RequestMapping("/emps")
	public String getEmps(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model) {
		// ����PageHelper���,���÷���,����ҳ���ÿҳ��С
		PageHelper.startPage(pn, 5);
		// ��ѯ��Ϊ��ҳ��ѯ
		List<Employee> emps = employeeService.getAll();
		// PageInfo��֤��ѯ����,��PageInfo����ҳ��,��װ����ϸ��ҳ��Ϣ
		PageInfo pageInfo = new PageInfo(emps, 5);
		model.addAttribute("pageInfo", pageInfo);

		return "list";
	}

}

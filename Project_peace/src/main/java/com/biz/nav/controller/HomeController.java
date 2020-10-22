package com.biz.nav.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.biz.nav.model.MemberVO;
import com.biz.nav.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {
	
	@Autowired
	@Qualifier("mServiceV1")
	private MemberService mService;
	
	
	// 회원가입
	@RequestMapping(value = "/", method=RequestMethod.GET)
	public String join(@ModelAttribute("MEM_VO") MemberVO memVO, Model model) {
		
		model.addAttribute("BODY", "MEM_WRITE");
	//	model.addAttribute("BODY", "LOGIN");
		return "home";
		
	}
	// 회원가입
	@RequestMapping(value = "/", method=RequestMethod.POST)
	public String join(@ModelAttribute("MEM_VO") MemberVO memVO, Model model, String s) {
			
		log.debug(memVO.toString());
		mService.insert(memVO);
		
		
		
		return "redirect:/";
	}
	
	@ResponseBody
	// 로그인 완료 된 상태에서 가는 map page
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@ModelAttribute("MEM_VO") MemberVO loginVO, MemberVO memVO, Model model,
			HttpSession httpSession) {
		
		log.debug("정보 : " + memVO.toString());
		
		memVO = mService.login(loginVO);
		

		httpSession.setAttribute("LOGIN", memVO);
		
		return "1";
	}
	
	
	@RequestMapping(value = "/logout", method=RequestMethod.GET)
	public String logout(@ModelAttribute("MEM_VO") MemberVO loginVO, MemberVO memVO,
			HttpSession httpSession) {
		httpSession.setAttribute("LOGIN", null);
		log.debug("로그아웃성공");
		log.debug(memVO.toString());
		
		return "redirect:/";
	}
	
	 
	@RequestMapping(value = "/map",method=RequestMethod.GET)
	public String map() {
		
		return "map";
	}
	
	@RequestMapping(value = "/mypage",method=RequestMethod.GET)
	public String mypage(@ModelAttribute("MEM_VO") MemberVO memVO, HttpSession httpSession) {
		log.debug("고객정보 확인 : " + httpSession.toString());
		
		
		return "mypage";
	}
	
	@RequestMapping(value = "/memberDelete",method = RequestMethod.GET)
	public String delete() {
		
		return "/deleteView";
	}
	@RequestMapping(value = "/memberDelete",method=RequestMethod.POST)
	public String delete(MemberVO memVO, HttpSession httpSession, RedirectAttributes rttr) {
		
		// 세션에 있는 member를 가져와서 member변수에 넣어줌
		MemberVO member = (MemberVO) httpSession.getAttribute("LOGIN");
		// 세션에 있는 비밀번호
		String sessionPass = member.getN_password();
		// memVO로 돌아오는 비밀번호
		String memPass = memVO.getN_password();
		
		if(!(sessionPass.equals(memPass))) {
			rttr.addFlashAttribute("msg", false);
			return "redirect:/memberDelete";
		}
		
		mService.delete(memVO);
		httpSession.invalidate();
		return "redirect:/";
	}
	
	
	
}
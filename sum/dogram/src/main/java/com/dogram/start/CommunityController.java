package com.dogram.start;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.http.HttpCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.dao.CommunityDao;
import model.dto.CommentDto;
import model.dto.CommunityDto;
import model.dto.LikeListDto;
import model.dto.UserDto;
import service.CommentService;
import service.CommunityService;
import service.LikeListService;
import service.UserService;

@CrossOrigin
@Controller
@RequestMapping(value="/feed", produces="application/json; charset=utf-8")
public class CommunityController {

//	@GetMapping("/newfeed")
//	public String community() {
//		return "newfeed";
//	}
	
	
	private static final String FILE_SERVER_PATH = "/home/cat/img";
	private static String img = null;
	
//	@RequestMapping("/upload")
//	public String upload(@RequestParam("uploadFile") MultipartFile file, ModelAndView mv, Model model) throws IllegalStateException, IOException {
//		if(!file.getOriginalFilename().isEmpty()) {
//			file.transferTo(new File("/home/cat/img", file.getOriginalFilename()));
//			model.addAttribute("msg", "File uploaded successfully.");
//			img = FILE_SERVER_PATH+"/"+file.getOriginalFilename();
//		}else {
//			model.addAttribute("msg", "Please select a valid mediaFile..");
//		}
//		
//		return "join";
//	}
	
	
	@PostMapping("/newfeed")
	@ResponseBody
	public int newfeed(@CookieValue(value="id", required=false) Cookie cookie,
			@RequestParam("img") MultipartFile img, HttpServletRequest file,Model model,ModelAndView mv) {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:application-context.xml");
		CommunityService comm = ctx.getBean("community",CommunityService.class);
		
//		HttpSession session = request.getSession();
//		String id = (String) session.getAttribute("id");
		
		CommunityDto cdto = new CommunityDto();
		
		cdto.setTitle(file.getParameter("title"));
		cdto.setContent(file.getParameter("content"));
		cdto.setCategory(file.getParameter("category"));
		cdto.setAddress(file.getParameter("address"));
		
		System.out.println("1");
		
//		if(cookie.getName() != null) {
//		if(cookie.getName().equals("id")){
			try {
				System.out.println("2");
								
//				Long userNum = comm.ckeckCookie(cookie.getValue());
//				Long userNum = comm.ckeckCookie(id);
				Long userNum = comm.ckeckCookie("hhh");
				cdto.setImg(comm.upload(img, mv,model));
				int num = comm.create(cdto, userNum);
				return num;
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -2;
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		}
//		}
			System.out.println("3");
		return -1;
	}
	

	
	
	
	@PostMapping("/")
	@ResponseBody
	public List<CommunityDto> read(@CookieValue(value="id", required=false) Cookie cookie,@RequestBody UserDto userDto,CommunityDto dto,HttpServletRequest request ) {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:application-context.xml");
		CommunityService comm = ctx.getBean("community",CommunityService.class);
		
//		HttpSession session = request.getSession();
//		String id = (String) session.getAttribute("id");
		String id = "hhh";
		
		if(cookie.getName() != null) {
				try {
//					Long userNum = comm.ckeckCookie(cookie.getValue());
					Long userNum = comm.ckeckCookie(id);
					userDto.setNum(userNum);
					dto.setUserNum(userNum);
					List<CommunityDto> list = comm.read(userDto,dto);
					
					return list;
					
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				
				}
			
		}
		return null;
		
	}
	
	
	@PostMapping("/addlike")
	@ResponseBody
	public int addlike(@RequestBody LikeListDto dto,@CookieValue(value="id", required=false) Cookie cookie,HttpServletRequest request ) {
		
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:application-context.xml");
		LikeListService like = ctx.getBean("likeList",LikeListService.class);
		
//		HttpSession session = request.getSession();
//		String id = (String) session.getAttribute("id");
		String id = "hhh";
		
		int num = -1;
		if(cookie.getName() != null) {
			try {
//				Long uNum = like.ckeckCookie(cookie.getValue());
				Long uNum = like.ckeckCookie(id);
				dto.setUserNum(uNum);
				Long c = like.checkLike(dto);
					if(c == 0L) num = like.create(dto);
					else num = like.delete(dto);
					
				return num;
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -1;
			}
		}
//		
		return -2;
	}
	
	
	@PostMapping("/update")
	@ResponseBody
	public int update(@RequestBody CommunityDto dto,@CookieValue(value="id", required=false) Cookie cookie,HttpServletRequest request) {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:application-context.xml");
		CommunityService comm = ctx.getBean("community",CommunityService.class);
		
		if(img != null) {
			dto.setImg(img);
			img = null;
		}
		
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");
//		if(cookie.getName() != null) {
//		if(cookie.getName().equals("id")){
			try {
//				Long userNum = comm.ckeckCookie(cookie.getValue());
				Long userNum = comm.ckeckCookie(id);
				dto.setUserNum(userNum);
				int num = comm.update(dto);
				return num;
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -2;
			}
//		}
//		}
//		return -1;
	}
	
	
	@PostMapping("/delete")
	@ResponseBody
	public int delete(@RequestBody CommunityDto dto,@CookieValue(value="id", required=false) Cookie cookie,HttpServletRequest request) {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:application-context.xml");
		CommunityService comm = ctx.getBean("community",CommunityService.class);
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");
		
//		if(cookie.getName() != null) {
			try {
				Long userNum = comm.ckeckCookie(id);
				dto.setUserNum(userNum);
				int num = comm.delete(dto);
				return num;
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -2;
			}
//		}
		
//		return -1;
	}
	
	
	@PostMapping("/commentadd")
	@ResponseBody
	public int addComment(@CookieValue(value="id", required=false) Cookie cookie,@RequestBody CommentDto dto,HttpServletRequest request) {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:application-context.xml");
		CommentService comme = ctx.getBean("comment",CommentService.class);
		int num = -1;
		
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");
//		if(cookie.getName() != null) {
			try {
				comme.ckeckCookie(cookie.getValue(),dto);
				comme.ckeckCookie(id,dto);
				num = comme.create(dto);
					
				return num;
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -2;
			}
	
		
//		}
	}
	
	@PostMapping("/comment")
	@ResponseBody
	public List<CommentDto> readComment(@CookieValue(value="id", required=false) Cookie cookie,@RequestBody CommentDto dto,HttpServletRequest request) {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:application-context.xml");
		CommentService comme = ctx.getBean("comment",CommentService.class);
		
		List<CommentDto> list = null;
		
		int num = -1;
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("id");
//		if(cookie.getName() != null) {
			try {
//				comme.ckeckCookie(cookie.getValue(),dto);
//				comme.ckeckCookie(id,dto);
				list = comme.read(dto);
				return list;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return list;
			}
	
		
	}
	
	
	
	@PostMapping("/commentdel")
	@ResponseBody
	public int delComment(@CookieValue(value="id", required=false) Cookie cookie,@RequestBody CommentDto dto,HttpServletRequest request) {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:application-context.xml");
		CommentService comme = ctx.getBean("comment",CommentService.class);
		int num = -1;
		
		HttpSession session = request.getSession();
//		String id = (String) session.getAttribute("id");
		String id = cookie.getValue();
		System.out.println(id);
//		if(cookie.getName() != null) {
			try {
//				comme.ckeckCookie(cookie.getValue(),dto);
				Long user = comme.ckeckself(id,dto);
				if(user == dto.getUserNum()) {
					comme.delete(dto);
					return 1;
				}
				else {
					System.out.println(user + "   "+dto.getUserNum());
					return num;
				}
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -2;
			}
	
		
//		}n -1;
	}
	
	
	@PostMapping("/commentup")
	@ResponseBody
	public int upComment(@CookieValue(value="id", required=false) Cookie cookie,@RequestBody CommentDto dto,HttpServletRequest request) {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:application-context.xml");
		CommentService comme = ctx.getBean("comment",CommentService.class);
		
		HttpSession session = request.getSession();
//		String id = (String) session.getAttribute("id");
		String id = cookie.getValue();
		
		try {
//			comme.ckeckCookie(cookie.getValue(),dto);
			Long user = comme.ckeckself(id,dto);
			if(user == dto.getUserNum()) {
				comme.upComment(dto);
				return 1;
			}
			else {
				System.out.println(user + "   "+dto.getUserNum());
				return -1;
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -2;
		}
		
		
		
//		return -1;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
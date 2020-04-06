package cn.ncepu.voluntize.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Controller
public class Pages {
    @RequestMapping(value = "/errors", method = RequestMethod.GET)
    public String error(Model model, @RequestParam(name = "message") String message) throws UnsupportedEncodingException {
        String m = URLDecoder.decode(message,"utf-8");
        model.addAttribute("message", m);
        return "error";
    }
}

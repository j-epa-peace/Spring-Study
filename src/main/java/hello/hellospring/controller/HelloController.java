package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello!!");
        return "hello";
    }

    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model){
        model.addAttribute("name", name);
        return "hello-mvc";
    }

    @GetMapping("hello-string")
    @ResponseBody
    public String helloString(@RequestParam("name") String name){
        return "hello " + name;
    }

    @GetMapping("hello-api")
    @ResponseBody
    public Hello2 helloApi(@RequestParam("na") String nass) {
        Hello2 hello = new Hello2();
        hello.setSdf(nass);
        return hello;
    }

    static class Hello {
        private String nasdfss;

        public String getName() {
            return nasdfss;
        }

        public void setName(String nass) {
            this.nasdfss = nass;
        }
    }

    static class Hello2 {
        private String nasdfss;
        private String sdf;
        private String name;

        public String getSdf() {
            return sdf;
        }
        public void setSdf(String sdf) {
            this.sdf = sdf;
        }
        public String getNasdfss() {
            return nasdfss;
        }
        public void setNasdfss(String nasdfss) {
            this.nasdfss = nasdfss;
        }
        public String getName() {
            return name;
        }
        public void setName(String nass) {
            this.name = nass;
        }
    }
}

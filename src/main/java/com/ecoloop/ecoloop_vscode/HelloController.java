package com.ecoloop.ecoloop_vscode;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class HelloController {

    private static final @Nullable String UPLOAD_DIR = null;

    @GetMapping("/index")
    public String index() {
        return "index";  
    }

    @PostMapping("/index")
    public String login() {
        return "redirect:/dashboard";  
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; 
    }

    @GetMapping("/envios")
    public String envios() {
        return "envios";
}

    @GetMapping("/beneficios")
    public String beneficios() {
        return "beneficios";
}

        @GetMapping("/conquistas")
    public String conquistas() {
        return "conquistas";
}

   @GetMapping("/perfil")
    public String perfil() {
        return "perfil";
}



    @PostMapping("/upload")
    public String upload(@RequestParam("files") MultipartFile[] files) {

        for (MultipartFile file : files) {

            if (!file.isEmpty()) {

                try {
                    File saveFile = new File(UPLOAD_DIR + file.getOriginalFilename());
                    file.transferTo(saveFile);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "redirect:/envio?success=true";
    }
}


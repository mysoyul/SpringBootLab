package com.rookies3.myspringbootlab.controller;

import com.rookies3.myspringbootlab.controller.dto.UserDTO;
import com.rookies3.myspringbootlab.entity.User;
import com.rookies3.myspringbootlab.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserThymeleafController {

    private final UserService userService;

    // 사용자 목록 페이지
    @GetMapping
    public String listUsers(Model model) {
        List<UserDTO.UserResponse> users = userService.getAllUsers().stream()
                .map(UserDTO.UserResponse::new)
                .toList();
        model.addAttribute("users", users);
        return "users/list";
    }

    // 사용자 등록 폼
    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("userCreateRequest", new UserDTO.UserCreateRequest());
        return "users/create";
    }

    // 사용자 등록 처리
    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("userCreateRequest") UserDTO.UserCreateRequest request,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "users/create";
        }

        try {
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            
            userService.createUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "사용자가 성공적으로 등록되었습니다.");
            return "redirect:/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "사용자 등록 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/users/create";
        }
    }

    // 사용자 상세 조회
    @GetMapping("/{id}")
    public String getUserDetails(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", new UserDTO.UserResponse(user));
        return "users/detail";
    }

    // 사용자 수정 폼
    @GetMapping("/{id}/edit")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        
        UserDTO.UserUpdateRequest updateRequest = new UserDTO.UserUpdateRequest();
        updateRequest.setName(user.getName());
        
        model.addAttribute("userUpdateRequest", updateRequest);
        model.addAttribute("userId", id);
        model.addAttribute("userEmail", user.getEmail());
        return "users/edit";
    }

    // 사용자 수정 처리
    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute("userUpdateRequest") UserDTO.UserUpdateRequest request,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "users/edit";
        }

        try {
            User userDetail = new User();
            userDetail.setName(request.getName());
            
            User user = userService.getUserById(id);
            userService.updateUserByEmail(user.getEmail(), userDetail);
            
            redirectAttributes.addFlashAttribute("successMessage", "사용자 정보가 성공적으로 수정되었습니다.");
            return "redirect:/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "사용자 정보 수정 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/users/" + id + "/edit";
        }
    }

    // 사용자 삭제 확인 페이지
    @GetMapping("/{id}/delete")
    public String deleteConfirmForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", new UserDTO.UserResponse(user));
        return "users/delete-confirm";
    }

    // 사용자 삭제 처리
    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "사용자가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "사용자 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
        return "redirect:/users";
    }
}
package com.meidesaqua.meidesaqua_backend.controller;

import lombok.Data;

@Data
public class UpdateAvatarRequest {
    private String avatar; // O frontend enviará: {"avatar": "avatar-homem.png"}
}
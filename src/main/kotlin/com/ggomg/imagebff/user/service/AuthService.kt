package com.ggomg.imagebff.user.service

interface AuthService {
    
    fun registerNormalUser(name: String, email: String, password: String)
    
}
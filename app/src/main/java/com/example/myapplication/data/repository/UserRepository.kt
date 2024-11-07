package com.example.myapplication.data.repository

import android.app.Application
import android.util.Log
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.UserDao
import com.example.myapplication.data.model.DTO.UserDTO
import com.example.myapplication.data.model.entity.UserEntity
import com.example.myapplication.data.model.mapper.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.mail.Authenticator
import java.util.Properties
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.PasswordAuthentication
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class UserRepository(application : Application) {
    private val userDao: UserDao = AppDatabase.getDatabase(application).userDao()

    // viewModel 에 받아온 유저 DTO를 유저 엔티티로 변환하고 UserDao 에 전달
    suspend fun registerUser(userDTO: UserDTO) {
        val userEntity = userDTO.toEntity();
        userDao.insertUser(userEntity);
    }

    suspend fun sendVerificationCode(email: String): String? {
        val exists = isEmailExists(email)

        return if (exists) {
            val verificationCode = generateVerificationCode()
            sendEmail(email, verificationCode)
            verificationCode
        } else  {
            null
        }
    }

    suspend fun isEmailExists(email: String): Boolean {
        return userDao.isEmailExists(email)
    }
    suspend fun isUserExist(userName : String, email: String) : Boolean {
        return userDao.getUserByIdAndEmail(userName,email) != null
    }
    private fun generateVerificationCode(): String{
        return (100000..999999).random().toString()
    }
    private fun sendEmail(email: String,verificationCode : String){
        val smtpHost = "smtp.gmail.com"
        val smtpPort = "587"
        val fromEmail = "testdju2024@gmail.com" // 발신지 이메일 적어야함
        val password = "qweasdzxc12!" // 발신지 이메일의 비밀번호 적어야함

        val properties = Properties().apply{
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", smtpHost)
            put("mail.smtp.port", smtpPort)
        }
       val session = Session.getInstance(properties, object : Authenticator() {
           override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(fromEmail, password)
            }
        })
        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(fromEmail))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(email))
                subject = "animalKid: 인증번호"
                setText("인증번호: $verificationCode")
            }

            Transport.send(message)

        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }


    suspend fun getUserByUsername(userName: String): UserEntity? {
        return userDao.getUserByUsername(userName)
    }
    suspend fun  getUserByUserId(userId : String?): UserEntity? {
        return userDao.getUserByUserId(userId)
    }

}
package com.example.myapplication.data.repository

import android.app.Application
import android.util.Log
import com.example.myapplication.BuildConfig
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.UserDao
import com.example.myapplication.data.model.DTO.ProfileDTO
import com.example.myapplication.data.model.DTO.UserDTO
import com.example.myapplication.data.model.entity.ProfileEntity
import com.example.myapplication.data.model.entity.UserEntity
import com.example.myapplication.data.model.mapper.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        try {
            val userEntity = userDTO.toEntity() // 변환
            Log.d("UserRepository", "Inserting user: $userEntity")
            userDao.insertUser(userEntity) // 삽입
            Log.d("UserRepository", "User inserted successfully")
        } catch (e: Exception) {
            Log.e("UserRepository", "Error inserting user: ${e.message}", e)
            throw e
        }
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
    private fun sendEmail(email: String, verificationCode: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val smtpHost = BuildConfig.SMTP_HOST
            val smtpPort = BuildConfig.SMTP_PORT
            val fromEmail = BuildConfig.FROM_EMAIL // 발신자 이메일
            val password = BuildConfig.PASSWORD // Gmail 앱 비밀번호를 입력

            val properties = Properties().apply {
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
                Log.d("SendEmail", "이메일 전송 성공")

            } catch (e: MessagingException) {
                Log.e("SendEmail", "이메일 전송 실패: ${e.message}", e)
            } catch (e: Exception) {
                Log.e("SendEmail", "예상치 못한 오류 발생: ${e.message}", e)
            }
        }
    }


    suspend fun getUserByUsername(userName: String): UserEntity? {
        return withContext(Dispatchers.IO) {
            userDao.getUserByUsername(userName)
        }
    }
    suspend fun  getUserByUserId(userId : String?): UserEntity? {
        return userDao.getUserByUserId(userId)
    }

    suspend fun updateProfile(profileDTO: ProfileDTO) {
        val profileEntity = profileDTO.toEntity()

        withContext(Dispatchers.IO) {
            try {
                // userId 유효성 검사: 해당 userId가 users 테이블에 존재하는지 확인
                val userExists = userDao.getUserByUserId(profileDTO.userId) != null
                if (!userExists) {
                    Log.e("UserRepository", "Invalid userId: ${profileDTO.userId}. User does not exist.")
                    throw IllegalStateException("Invalid userId: ${profileDTO.userId}. User does not exist.")
                }
                // 프로필 삽입 (존재하면 업데이트, 없으면 삽입)
                userDao.insertProfile(profileEntity)
                Log.d("UserRepository", "Profile updated successfully in database for userId: ${profileDTO.userId}")
            } catch (e: IllegalStateException) {
                Log.e("UserRepository", "Error updating com.example.myapplication.ui.fragment.profile: ${e.message}")
                throw e // 또는 사용자에게 적절한 피드백 제공
            } catch (e: Exception) {
                Log.e("UserRepository", "Unexpected error updating com.example.myapplication.ui.fragment.profile in database: ${e.message}", e)
            }
        }
    }



}
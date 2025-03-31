package com.ggomg.imagebff.user.repository

import com.ggomg.imagebff.user.entity.AuthType
import com.ggomg.imagebff.user.entity.User
import com.ggomg.imagebff.user.entity.UserRole
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@DataJpaTest
class UserRepositoryTest(
    @Autowired
    val userRepository: UserRepository
) {

    @Test
    fun `이메일 중복확인 테스트`() {
        // given
        val user = User(
            name = "kim",
            email = "hello@gmail.com",
            password = "password",
            authType = AuthType.NORMAL,
            userRole = UserRole.ROLE_USER
        )
        userRepository.save(user)

        // when
        val isExistsByEmail = userRepository.existsByEmail("hello@gmail.com")

        // then
        assertNotNull(isExistsByEmail)
        assertTrue(isExistsByEmail)
    }

    @Test
    fun `user 저장 후 이메일로 조회`() {
        // given
        val user = User(
            name = "kim",
            email = "hello@gmail.com",
            password = "password",
            authType = AuthType.NORMAL,
            userRole = UserRole.ROLE_USER
        )
        userRepository.save(user)

        // when
        val foundUser = userRepository.findByEmail("hello@gmail.com")

        // then
        assertNotNull(foundUser)
        assertEquals(foundUser.email, "hello@gmail.com")
    }
}
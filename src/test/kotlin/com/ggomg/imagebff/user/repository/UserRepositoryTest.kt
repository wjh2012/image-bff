package com.ggomg.imagebff.user.repository

import com.ggomg.imagebff.user.domain.AuthType
import com.ggomg.imagebff.user.infrastructure.entity.UserEntity
import com.ggomg.imagebff.user.domain.UserRole
import com.ggomg.imagebff.user.infrastructure.repository.UserJpaRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@DataJpaTest
class UserRepositoryTest(
    @Autowired
    val userJpaRepository: UserJpaRepository
) {

    @Test
    fun `이메일 중복확인 테스트`() {
        // given
        val userEntity = UserEntity(
            name = "kim",
            email = "hello@gmail.com",
            password = "password",
            authType = AuthType.NORMAL,
            userRole = UserRole.ROLE_USER
        )
        userJpaRepository.save(userEntity)

        // when
        val isExistsByEmail = userJpaRepository.existsByEmail("hello@gmail.com")

        // then
        assertNotNull(isExistsByEmail)
        assertTrue(isExistsByEmail)
    }

    @Test
    fun `user 저장 후 이메일로 조회`() {
        // given
        val userEntity = UserEntity(
            name = "kim",
            email = "hello@gmail.com",
            password = "password",
            authType = AuthType.NORMAL,
            userRole = UserRole.ROLE_USER
        )
        userJpaRepository.save(userEntity)

        // when
        val foundUser = userJpaRepository.findByEmail("hello@gmail.com")

        // then
        assertNotNull(foundUser)
        assertEquals(foundUser.email, "hello@gmail.com")
    }
}
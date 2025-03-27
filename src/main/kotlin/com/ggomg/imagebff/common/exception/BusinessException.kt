import com.ggomg.imagebff.common.exception.ErrorCode

class BusinessException(
    val errorCode: ErrorCode,
    message: String? = null
) : RuntimeException(message)
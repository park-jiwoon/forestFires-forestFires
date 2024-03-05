package com.ZeroWaveV2.FireAlertV2.constants;

public final class ErrorMessages {
	public static final String CONTACT_REQUIRED = "연락처는 필수 입력 값입니다.";
	public static final String FS_REQUIRED = "FS는 필수 입력 값입니다.";
	public static final String PASSWORD_REQUIRED = "비밀번호는 필수 입력 값입니다.";
    public static final String PASSWORD_MIN_LENGTH = "비밀번호는 최소 3자 이상이어야 합니다.";
    public static final String USER_NAME_REQUIRED = "사용자 이름은 필수 입력 값입니다.";
    public static final String USER_ALREADY_EXISTS = "이미 존재하는 연락처 입니다.";
    public static final String AUTHENTICATION_FAILED = "로그인에 실패했습니다. 연락처 또는 비밀번호를 확인해주세요.";
    // 추가적인 에러 메시지 상수들...

    private ErrorMessages() {
        // 이 클래스는 인스턴스화되어서는 안 됩니다.
    }
}

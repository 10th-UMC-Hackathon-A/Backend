## Git Convention

### Branch Convention
- **배포 브랜치**: `main`
- **개발 브랜치**: `develop`
- **작업 브랜치**: `커밋유형/#이슈번호-설명`
  - 예시: `feature/#3-social-login-api`

### Coding Convention
- **패키지명**: lowercase
  - `com.example.backend.user`
- **파일명**: UpperCamelCase
  - `UserController.java`, `AuthService.java`
- **Class / Interface**: UpperCamelCase
  - `UserController`, `AuthService`, `UserRepository`
- **메서드 / 변수명**: lowerCamelCase
  - `getUserById`, `accessToken`
- **상수명**: UPPER_SNAKE_CASE
  - `TOKEN_EXPIRE_TIME`
- **Enum 값**: UPPER_SNAKE_CASE
  - `ACTIVE`, `DELETED`, `PENDING`

#### Spring Boot Layer Naming
- **Controller**: `XXXController`
  - `UserController`, `AuthController`
- **Service**: `XXXService`
  - `UserService`, `AuthService`
- **Service 구현체가 필요한 경우**: `XXXServiceImpl`
  - `UserServiceImpl`
- **Repository**: `XXXRepository`
  - `UserRepository`
- **Entity**: 도메인명 단수형
  - `User`, `Post`
- **DTO**: 요청/응답 목적이 드러나게 작성
  - `UserCreateRequest`, `UserResponse`, `LoginRequest`
- **Config**: `XXXConfig`
  - `SecurityConfig`, `SwaggerConfig`

#### Method Naming
- `createXXX()`: 생성
- `getXXX()`: 단일 조회
- `getXXXList()`: 목록 조회
- `updateXXX()`: 수정
- `deleteXXX()`: 삭제
- `findXXX()`: 조건 검색
- `validateXXX()`: 검증
- `existsXXX()`: 존재 여부 확인

#### API / Server Logic
- `GET`: 조회
  - `getUser()`, `getUserList()`
- `POST`: 생성 및 요청 처리
  - `createUser()`, `login()`
- `PUT / PATCH`: 수정
  - `updateProfile()`
- `DELETE`: 삭제
  - `deleteUser()`

#### Annotation Convention
- Controller에는 `@RestController`, `@RequestMapping`을 사용한다.
- Service에는 `@Service`, Repository에는 `@Repository`를 사용한다.
- 트랜잭션이 필요한 비즈니스 로직에는 `@Transactional`을 사용한다.
- DTO 검증에는 `@Valid`, `@NotBlank`, `@NotNull` 등 Bean Validation을 사용한다.

---

### Issue Convention
- 형식 | **[커밋유형] 이슈 내용**
- 예시 | **[FEAT] 사용자 로그인 API 구현**

---

### PR Convention
1. PR 타입
   - 기능 추가
   - 기능 삭제
   - 버그 수정
   - 리팩토링 / 설정 변경
2. 반영 브랜치
   - `feature/#9-social-login-api` -> `develop`
3. 변경 사항
   - 변경 내용 요약
4. 테스트 결과
   - API 테스트 결과 또는 Swagger 캡처

---

### Commit Convention
1. 커밋 유형
   - Feat
   - Fix
   - Remove
   - Refactor
   - Style
   - Test

2. 커밋 메시지
- `Feat: 사용자 로그인 API 구현 (#9)`

3. 규칙
- 제목은 50자 이내로 작성
- 제목 끝에는 마침표를 사용하지 않음
- 본문에는 무엇을 왜 변경했는지 작성

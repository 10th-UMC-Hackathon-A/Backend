## Git Convention

### Branch Convention
- **배포용**: main
- **개발용**: develop
- **작업용**: 커밋유형/#이슈번호-설명  
  - 예) feature/#3-소셜로그인API구현

### Coding Convention
- **파일명**: kebab-case  
  - user.controller.ts
- **Class / Interface**: UpperCamelCase  
  - UserController, AuthService
- **함수 / 변수**: lowerCamelCase  
  - getUserById, accessToken
- **상수**: UPPER_SNAKE_CASE  
  - TOKEN_EXPIRE_TIME

#### 함수 네이밍
- initXXX(): 초기 설정
- createXXX(): 생성
- getXXX(): 단일 조회
- getXXXs(): 복수 조회
- updateXXX(): 수정
- deleteXXX(): 삭제
- findXXX(): 조건 탐색

#### 서버 로직
- GET → getUser(), getUserList()
- POST → createUser(), loginUser()
- PUT / PATCH → updateProfile()
- DELETE → deleteUser()

---

### Issue Convention
- 양식 | **[커밋유형] 이슈 내용**
- 예시 | **[FEAT] 사용자 로그인 API 구현**

---

### PR Convention
1. PR 타입
   - 기능 추가
   - 기능 삭제
   - 버그 수정
   - 리팩토링 / 설정 변경
2. 반영 브랜치  
   - feature/#9-소셜로그인API구현 → develop
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
- 제목 50자 이내
- 제목 끝 마침표 금지
- 본문에 무엇/왜 작성

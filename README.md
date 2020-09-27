# Banksalad_Android_ParkHyowan

### - 회원가입 및 정보 화면 구현

minAPI : 26
<br>

### 1. RxBinding을 사용한 이유

사용자가 입력하면서 생기는 이벤트들을 반응성으로 처리하고 싶어서 사용했습니다.



### 2. 로컬 저장소 선택 이유

처음에 로컬 저장소는 `SharedPreference vs Room vs Realm` 이렇게 세가지로 생각했습니다.
그러나 SharedPreference를 선택한 것은 체크해야할 데이터 양이 이메일 필드 하나이기 때문에 SharedPreference로 사용했습니다.


### 3. 라이브러리를 사용한 이유

제가 사용한 라이브러리는 'RxBinding','anko' 입니다. Rxbinding은 view 이벤트를 Observable 형태로 바꾸기 위해 사용했습니다.
'anko'는 intent와 toast를 사용할 때 간단한 코드로 사용하고 싶어서 사용했습니다.

### 4. 느낀점

코틀린으로 첫 개발을 하게 됐습니다. 코틀린으로 토이 프로젝트 한 번 구현해야지 싶다가도 번번이 우선순위에서 밀렸는데 이번 사전 과제를 통해 개발할 수 있게 되어 재밌었습니다!
말로만 들었던 코틀린으로 실제로 코딩해보니 너무 편하고 간단했습니다. 재밌는 경험이었습니다. 

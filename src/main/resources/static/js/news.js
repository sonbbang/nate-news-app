
let isRefreshing = false; // 새로고침 여부
let isLoading = false; // 추가 기사 로드 여부

// 새로 고침 버튼 클릭 이벤트 처리
document.getElementById('refresh-btn').addEventListener('click', () => {
    if (isRefreshing || isLoading) return; // 새로고침 중이거나 로딩 중이면 실행하지 않음
    refreshArticles(); // 새로고침 함수 호출
});

// 로딩 인디케이터 표시
function showLoadingIndicator() {
    const loadingIndicator = document.createElement('div');
    loadingIndicator.id = 'pull-to-refresh-indicator';
    loadingIndicator.textContent = '새로고침 중...';
    loadingIndicator.style.cssText = `
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      padding: 10px;
      text-align: center;
      background: #1a73e8;
      color: white;
      z-index: 1000;
    `;
    document.body.appendChild(loadingIndicator);
}

// 로딩 인디케이터 숨김
function hideLoadingIndicator() {
    const loadingIndicator = document.getElementById('pull-to-refresh-indicator');
    if (loadingIndicator) {
        loadingIndicator.remove();
    }
}

// 기사 새로고침 함수
function refreshArticles() {

    if (isLoading) return; // 중복 실행 방지
    isRefreshing = true;

    currentPage = 0; // 페이지 초기화
    hasMoreArticles = true; // 더 불러올 수 있게 설정
    document.getElementById('news-container').innerHTML = ''; // 기존 기사 초기화

    showLoadingIndicator(); // 로딩 인디케이터 표시

    loadArticles(selectedCategory, selectedFunction, true);
    hideLoadingIndicator();
    isRefreshing = false;
}

// 카드를 관찰 대상으로 추가
function observeCards() {
    const cards = document.querySelectorAll('.card');
    cards.forEach(card => observer.observe(card));
}

let currentPage = 0;
let hasMoreArticles = true;
let selectedCategory = '';  // 선택된 카테고리 변수
let selectedFunction = '';

function updateCategoryTabs() {
    const categoryTabs = document.querySelectorAll('.category-tab'); // 모든 카테고리 탭 가져오기

    categoryTabs.forEach(tab => {
        const link = tab.querySelector('a'); // <a> 태그 선택
        const categoryValue = link.getAttribute('data-category'); // <a> 태그에서 data-category 속성 값 읽기

        if (categoryValue === selectedCategory) {
            tab.classList.add('active'); // 선택된 카테고리에 active 클래스 추가
        } else {
            tab.classList.remove('active'); // 다른 카테고리는 active 클래스 제거
        }
    });
}

function updateFunctionTabs() {
    const functionTabs = document.querySelectorAll('.function-tab'); // 모든 Function 탭 가져오기

    functionTabs.forEach(tab => {
        const link = tab.querySelector('a'); // <a> 태그 선택
        const functionValue = link.getAttribute('data-function'); // <a> 태그에서 data-function 속성 값 읽기

        if (functionValue === selectedFunction) {
            tab.classList.add('active'); // 선택된 Function에 active 클래스 추가
        } else {
            tab.classList.remove('active'); // 다른 Function는 active 클래스 제거
        }
    });
}

// 카테고리 선택 시 Ajax 요청과 함께 선택된 카테고리 전달
function loadArticlesWithCategory(element) {
    const clickedCategory = element.getAttribute('data-category');  // data-category 속성 값 가져오기

    currentPage = 0;  // 페이지를 초기화
    hasMoreArticles = true;  // 더 불러올 수 있게 설정
    document.getElementById('news-container').innerHTML = '';  // 기존 기사들 초기화

    // 모든 .category-tab 요소에서 'active' 클래스 제거
    const categoryTabs = document.querySelectorAll('.category-tab'); // 모든 카테고리 탭 가져오기
    categoryTabs.forEach(tab => tab.classList.remove('active'));

    // 현재 선택된 카테고리와 동일한 카테고리가 클릭되었을 경우
    if (clickedCategory === selectedCategory) {
        selectedCategory = '';  // 선택된 카테고리 해제
    } else {
        selectedCategory = clickedCategory;  // 새 카테고리로 설정
        const categoryTab = element.parentElement;  // 부모 요소인 <li> 찾기
        categoryTab.classList.add('active');  // 'active' 클래스 추가
    }

    loadArticles(selectedCategory, selectedFunction);  // 모든 기사 로드

    // URL에 선택된 카테고리 반영
    const newUrl = new URL(window.location.href);
    if (selectedCategory) {
        newUrl.searchParams.set('category', selectedCategory); // 카테고리 반영
    } else {
        newUrl.searchParams.delete('category'); // 카테고리 해제 시 URL에서 제거
    }

    history.pushState(null, '', newUrl.toString());  // 브라우저 히스토리 업데이트
}



// Function 선택 시 Ajax 요청과 함께 선택된 Function 전달
function loadArticlesWithFunction(element) {
    const clickedFunction = element.getAttribute('data-function');  // data-function 속성 값 가져오기

    currentPage = 0;  // 페이지를 초기화
    hasMoreArticles = true;  // 더 불러올 수 있게 설정
    document.getElementById('news-container').innerHTML = '';  // 기존 기사들 초기화

    // 모든 .function-tab 요소에서 'active' 클래스 제거
    const functionTabs = document.querySelectorAll('.function-tab'); // 모든 카테고리 탭 가져오기
    functionTabs.forEach(tab => tab.classList.remove('active'));

    // 현재 선택된 펑션 동일한 펑션가 클릭되었을 경우
    if (clickedFunction === selectedFunction) {
        selectedFunction = '';  // 선택된 Function 해제

    } else {
        selectedFunction = clickedFunction;  // 새 Function으로 설정
        const functionTab = element.parentElement;  // 부모 요소인 <li> 찾기
        functionTab.classList.add('active');  // 'active' 클래스 추가
    }

    loadArticles(selectedCategory, selectedFunction);  // 모든 기사 로드

    const newUrl = new URL(window.location.href);

    if (selectedFunction) {
        newUrl.searchParams.set('function', selectedFunction);
    } else {
        newUrl.searchParams.delete('function');
    }
    history.pushState(null, '', newUrl.toString());  // 브라우저 히스토리 업데이트
}


// Function to load more articles
function loadArticles(selectedCategory = '', selectedFunction = '', isRefresh = false) {
    console.log('loadArticles.isRefresh: ' + isRefresh)

    if (isLoading || !hasMoreArticles) return; // 중복 실행 방지

    isLoading = true;
    document.getElementById('loading').style.display = 'block'; // Show loading indicator


    let fetchUrl = `/api/v1/news?page=${currentPage + 1}`;

    console.log(fetchUrl);

    // 선택된 카테고리 및 제외할 기사 ID 추가
    const categoryParam = selectedCategory || ''; // undefined인 경우 빈 문자열로 설정
    if (categoryParam) {
        fetchUrl += `&category=${encodeURIComponent(categoryParam)}`;
    }

    const functionParam = selectedFunction || ''; // undefined인 경우 빈 문자열로 설정
    if (functionParam) {
        fetchUrl += `&function=${encodeURIComponent(functionParam)}`;
    }

    // 새로고침 시 refresh=true 추가
    if (isRefresh) {
        fetchUrl += `&refresh=true`;
    }

    fetch(fetchUrl)
        .then(response => response.json())
        .then(data => {
            const container = document.getElementById('news-container');

            if (data.articles.length > 0) {
                data.articles.forEach((article, index) => {
                    const card = document.createElement('div');
                    card.classList.add('card');
                    card.innerHTML = `
                            <input type="hidden" name="itemId" value="${article.itemId}">
                            <a href="${article.linkUrl}">
                                <img src="${article.imageUrl}" alt="Article Image" />
                                <div class="card-body">
                                    <h3>${article.title}</h3>
                                    <!--p>${article.snippet}</p-->
                                </div>
                            </a>
                            <span class="article-source">${ article.mediaName }</span> ·
                            <span class="article-date">${ article.timeAgo }</span>
                            <span class="like-btn" onclick="toggleLike(this)"></span> <!-- 하트 아이콘 -->
                        `;

                    container.appendChild(card);

                    // 10개마다 광고 삽입
                    if ((index + 1) % 10 === 0) {
                        const adContainer = document.createElement('div');
                        adContainer.classList.add('ad-container');

                        // 번갈아 가며 842200과 842201 사용
                        const adId = Math.random() < 0.5 ? 842200 : 842203;

                        adContainer.innerHTML = `
                              <iframe src="https://ads-partners.coupang.com/widgets.html?id=${adId}&template=carousel&trackingCode=AF2502891&subId=&width=900&height=900&tsource=" width="900" height="900" frameborder="0" scrolling="no" referrerpolicy="unsafe-url" browsingtopics></iframe>
                              `;
                        container.appendChild(adContainer);
                    }
                });

                observeCards(); // 새로 로드된 카드도 관찰
                observeTriggerArticle(); // 마지막에서 세 번째 기사 감지
                currentPage++;  // Increment current page after loading
            } else {
                hasMoreArticles = true;  // No more articles to load
            }

            isLoading = false;
            document.getElementById('loading').style.display = 'none';
        })
        .catch(() => {
            isLoading = false;
            document.getElementById('loading').style.display = 'none';
            console.error('Failed to load articles:', error); // 상세 에러 로그

            alert('Failed to load articles.');
        });
}


// 마지막에서 세 번째 기사를 감지하는 함수
function observeTriggerArticle() {
    const cards = document.querySelectorAll('.card'); // 모든 기사 목록 가져오기
    if (cards.length < 3) return; // 3개 미만이면 감지할 필요 없음

    const triggerArticle = cards[cards.length - 3]; // 마지막에서 세 번째 기사 선택

    const observer = new IntersectionObserver(entries => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                observer.disconnect(); // 중복 호출 방지
                console.log("observeTriggerArticle");
                loadArticles(selectedCategory, selectedFunction,);
            }
        });
    });

    observer.observe(triggerArticle);
}

// 좋아요 토글 함수
function toggleLike(button) {
    button.classList.toggle('liked');  // 클릭 시 'liked' 클래스를 토글하여 색상 변경
}

function debounce(func, delay) {
    let timer;
    return function (...args) {
        clearTimeout(timer);
        timer = setTimeout(() => func.apply(this, args), delay);
    };
}

// 스크롤 이벤트 핸들러
window.addEventListener('scroll', debounce(() => {
    if (isRefreshing || isLoading || !hasMoreArticles) return; // 새로고침 중이거나 로딩 중이거나 더 이상 기사가 없으면 실행 안 함

    const nearBottom = document.documentElement.scrollTop + window.innerHeight >= document.documentElement.scrollHeight - 100; // 100px로 여유를 두기

    if (nearBottom && hasMoreArticles && !isLoading) {
        loadArticles(selectedCategory, selectedFunction,);
    }
}, 200));


document.addEventListener('DOMContentLoaded', () => {
    setupTopButton();

    // MutationObserver 설정 (카테고리가 변경될 때 감지)
    const observer = new MutationObserver(() => {
        setupTopButton();
    });

    const newsContainerParent = document.querySelector("#news-container")?.parentNode;

    if (newsContainerParent) {
        observer.observe(newsContainerParent, { childList: true, subtree: true });
    }

    const urlParams = new URLSearchParams(window.location.search);
    const categoryFromUrl = urlParams.get('category'); // URL에서 카테고리 값 가져오기
    const functionFromUrl = urlParams.get('function');

    if (categoryFromUrl) {
        selectedCategory = categoryFromUrl;
        updateCategoryTabs();
    }

    if (functionFromUrl) {
        selectedFunction = functionFromUrl;
        updateFunctionTabs();
    }

    loadArticles(selectedCategory, selectedFunction,); // 선택된 카테고리로 초기화
});

function setupTopButton() {
    // 기존 버튼이 있으면 삭제
    let existingButton = document.querySelector("#topButton");
    if (existingButton) {
        return; // 이미 버튼이 있으면 다시 만들지 않음
    }

    const newsContainer = document.querySelector("#news-container");
    if (!newsContainer) {
        console.warn("스크롤 컨테이너(#news-container)를 찾을 수 없습니다.");
        return;
    }

    // Top 버튼 생성
    const topButton = document.createElement("button");
    topButton.id = "topButton";
    topButton.innerText = "↑";
    topButton.style.position = "fixed"; // 페이지 전체에서 유지
    topButton.style.bottom = "20px";
    topButton.style.right = "20px";
    topButton.style.width = "70px";
    topButton.style.height = "70px";
    topButton.style.borderRadius = "50%";
    topButton.style.display = "none"; // 초기 숨김
    topButton.style.alignItems = "center";
    topButton.style.justifyContent = "center";
    topButton.style.backgroundColor = "#007bff";
    topButton.style.color = "#fff";
    topButton.style.border = "none";
    topButton.style.cursor = "pointer";
    topButton.style.fontSize = "24px";
    topButton.style.boxShadow = "2px 2px 10px rgba(0, 0, 0, 0.2)";
    topButton.style.zIndex = "1000";

    document.body.appendChild(topButton); // body에 추가하여 유지됨

    // 스크롤 이벤트 리스너 추가
    newsContainer.addEventListener("scroll", function () {
        if (newsContainer.scrollTop > 100) {
            topButton.style.display = "flex"; // 스크롤 내리면 버튼 표시
        } else {
            topButton.style.display = "none"; // 상단이면 버튼 숨김
        }
    });

    // 버튼 클릭 시 `#news-container` 내부 스크롤 최상단 이동
    topButton.addEventListener("click", function () {
        newsContainer.scrollTo({ top: 0, behavior: "smooth" });

        // 추가 보정: 완전히 올라가지 않을 경우 대비
        const checkScroll = setInterval(() => {
            if (newsContainer.scrollTop === 0) {
                clearInterval(checkScroll);
            } else {
                newsContainer.scrollTop = 0;
            }
        }, 50);
    });
}


window.addEventListener('popstate', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const categoryFromUrl = urlParams.get('category'); // URL에서 카테고리 값 가져오기
    const functionFromUrl = urlParams.get('function'); // URL에서 카테고리 값 가져오기

    selectedCategory = categoryFromUrl || ''; // URL에 없으면 빈 문자열로 초기화
    updateCategoryTabs(); // 카테고리 탭 상태 업데이트

    selectedFunction = functionFromUrl || ''; // URL에 없으면 빈 문자열로 초기화
    updateFunctionTabs(); // 카테고리 탭 상태 업데이트

    document.getElementById('news-container').innerHTML = ''; // 기존 기사 초기화
    loadArticles(selectedCategory, selectedFunction); // 복구된 카테고리로 기사 로드
});
-- ============================================
-- MediGuard AI - 복약 안전 교육 영상 데이터
-- ============================================


SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE prevention_videos;
TRUNCATE TABLE video_categories;
SET FOREIGN_KEY_CHECKS = 1;


-- 카테고리 데이터 (id 명시적 추가)
INSERT INTO video_categories (id, name, description, icon, display_order, is_active, created_at, updated_at) VALUES
                                                                                                                 (1, '올바른 복약법',      '올바른 약 복용 방법 교육 영상',        'medication',      1, true, NOW(), NOW()),
                                                                                                                 (2, '약 부작용 & 주의사항', '약 부작용 및 주의사항 안내 영상',      'warning_amber',   2, true, NOW(), NOW()),
                                                                                                                 (3, '약물 상호작용',      '약물 간 상호작용 주의 교육 영상',       'compare_arrows',  3, true, NOW(), NOW()),
                                                                                                                 (4, '1인 가구 복약 관리',  '혼자 사는 분들을 위한 복약 관리 영상',  'home_health',     4, true, NOW(), NOW());


-- ============================================
-- 올바른 복약법 영상 (category_id = 1)
-- ============================================
INSERT INTO prevention_videos
(title, description, youtube_url, youtube_id, thumbnail_url, duration, view_count, display_order, is_active, category_id, created_at, updated_at)
VALUES
    ('약 올바르게 먹는 방법 - 식전 식후 차이점',
     '식전·식후·취침 전 복용 시간의 차이와 올바른 복약 기본 원칙을 설명하는 영상',
     'https://www.youtube.com/watch?v=kWBgBnECmcs', 'kWBgBnECmcs',
     'https://img.youtube.com/vi/kWBgBnECmcs/hqdefault.jpg', '5:12', 0, 1, true, 1, NOW(), NOW()),

    ('약은 왜 꼭 물과 함께 먹어야 할까?',
     '커피·우유·주스 등 물 이외의 음료와 약을 함께 먹으면 안 되는 이유를 쉽게 설명하는 영상',
     'https://www.youtube.com/watch?v=0Zt5s_o4hSk', '0Zt5s_o4hSk',
     'https://img.youtube.com/vi/0Zt5s_o4hSk/hqdefault.jpg', '4:38', 0, 2, true, 1, NOW(), NOW()),

    ('알약 쪼개거나 씹으면 안 되는 이유 - 서방형 제제란?',
     'SR·XR 표시 서방형 약을 쪼개면 안 되는 이유와 올바른 복용 방법을 설명하는 영상',
     'https://www.youtube.com/watch?v=89Z7Q-8Vd_E', '89Z7Q-8Vd_E',
     'https://img.youtube.com/vi/89Z7Q-8Vd_E/hqdefault.jpg', '4:20', 0, 3, true, 1, NOW(), NOW()),

    ('항생제는 왜 끝까지 먹어야 할까?',
     '항생제를 증상이 나아도 끝까지 복용해야 하는 이유와 내성균 발생 원리를 설명하는 영상',
     'https://www.youtube.com/watch?v=vXubjpbCWcs', 'vXubjpbCWcs',
     'https://img.youtube.com/vi/vXubjpbCWcs/hqdefault.jpg', '5:30', 0, 4, true, 1, NOW(), NOW()),

    ('안약(점안액) 올바르게 넣는 방법',
     '점안액을 올바르게 점안하는 방법과 두 종류 이상 안약 사용 시 간격 안내 영상',
     'https://www.youtube.com/watch?v=s-mS3Acs_kM', 's-mS3Acs_kM',
     'https://img.youtube.com/vi/s-mS3Acs_kM/hqdefault.jpg', '3:55', 0, 5, true, 1, NOW(), NOW()),

    ('복약 알림 앱 활용법 - 약 챙겨 먹는 습관 만들기',
     '스마트폰 복약 알림 앱 활용 방법과 복약 일지 작성으로 실수 없이 약 챙기는 방법 안내',
     'https://www.youtube.com/watch?v=5nGMXXeQhMw', '5nGMXXeQhMw',
     'https://img.youtube.com/vi/5nGMXXeQhMw/hqdefault.jpg', '4:45', 0, 6, true, 1, NOW(), NOW()),

    ('약 올바르게 보관하는 방법 - 욕실은 절대 안 돼요!',
     '약의 올바른 보관 장소, 냉장 보관 필요 약 구별법, 유효기간 확인 방법을 안내하는 영상',
     'https://www.youtube.com/watch?v=qvH1dkGGWVE', 'qvH1dkGGWVE',
     'https://img.youtube.com/vi/qvH1dkGGWVE/hqdefault.jpg', '4:10', 0, 7, true, 1, NOW(), NOW()),

    ('남은 약 올바르게 버리는 방법 - 폐의약품 처리법',
     '유효기간 지난 약·남은 약을 변기·쓰레기통에 버리면 안 되는 이유와 약국 반납 방법 안내',
     'https://www.youtube.com/watch?v=zHwKqMmqcAY', 'zHwKqMmqcAY',
     'https://img.youtube.com/vi/zHwKqMmqcAY/hqdefault.jpg', '3:58', 0, 8, true, 1, NOW(), NOW());


-- ============================================
-- 약 부작용 & 주의사항 영상 (category_id = 2)
-- ============================================
INSERT INTO prevention_videos
(title, description, youtube_url, youtube_id, thumbnail_url, duration, view_count, display_order, is_active, category_id, created_at, updated_at)
VALUES
    ('타이레놀 과다복용하면 무슨 일이 생길까?',
     '아세트아미노펜 성분 과다복용 시 간 손상 위험과 올바른 용량 준수의 중요성을 설명하는 영상',
     'https://www.youtube.com/watch?v=mVGXVhG9BUg', 'mVGXVhG9BUg',
     'https://img.youtube.com/vi/mVGXVhG9BUg/hqdefault.jpg', '5:02', 0, 1, true, 2, NOW(), NOW()),

    ('소염진통제 공복에 먹으면 안 되는 이유',
     'NSAIDs 계열 소염진통제를 빈속에 복용하면 위 점막이 손상되는 이유와 올바른 복용법 안내',
     'https://www.youtube.com/watch?v=TqXMxMoKBKg', 'TqXMxMoKBKg',
     'https://img.youtube.com/vi/TqXMxMoKBKg/hqdefault.jpg', '4:25', 0, 2, true, 2, NOW(), NOW()),

    ('약 먹고 졸릴 때 - 항히스타민제 부작용 정리',
     '항히스타민제(알레르기약·감기약)의 졸음 부작용, 운전 금지 이유, 안전한 복용 시간대 안내',
     'https://www.youtube.com/watch?v=JCfkRXr5HuE', 'JCfkRXr5HuE',
     'https://img.youtube.com/vi/JCfkRXr5HuE/hqdefault.jpg', '4:50', 0, 3, true, 2, NOW(), NOW()),

    ('약 부작용이 의심될 때 이렇게 하세요!',
     '약 복용 후 이상 증상 발생 시 올바른 대처 순서와 부작용 신고 방법(1644-6223) 안내',
     'https://www.youtube.com/watch?v=b1GU-qFmF8E', 'b1GU-qFmF8E',
     'https://img.youtube.com/vi/b1GU-qFmF8E/hqdefault.jpg', '5:15', 0, 4, true, 2, NOW(), NOW()),

    ('술과 약을 같이 먹으면 왜 위험한가?',
     '알코올과 각종 약(진통제·수면제·항생제 등)을 함께 복용했을 때 발생하는 위험한 반응 설명',
     'https://www.youtube.com/watch?v=a8ij2PNbNVk', 'a8ij2PNbNVk',
     'https://img.youtube.com/vi/a8ij2PNbNVk/hqdefault.jpg', '4:33', 0, 5, true, 2, NOW(), NOW()),

    ('혈압약·당뇨약 임의로 중단하면 생기는 일',
     '만성질환 약을 증상이 나아졌다고 임의로 중단했을 때 발생하는 위험한 상황과 사례 소개',
     'https://www.youtube.com/watch?v=Yf0Gg4y0kN8', 'Yf0Gg4y0kN8',
     'https://img.youtube.com/vi/Yf0Gg4y0kN8/hqdefault.jpg', '5:05', 0, 6, true, 2, NOW(), NOW()),

    ('임산부가 조심해야 할 약 - 임신 중 복약 가이드',
     '임신 중 복용 가능한 약과 금지 약물, 반드시 의사와 상담해야 하는 이유를 설명하는 영상',
     'https://www.youtube.com/watch?v=nCxQEzUu3Oc', 'nCxQEzUu3Oc',
     'https://img.youtube.com/vi/nCxQEzUu3Oc/hqdefault.jpg', '6:12', 0, 7, true, 2, NOW(), NOW()),

    ('약물 과다복용 응급상황 대처법 - 119와 1339',
     '약물 과다복용 의심 시 즉시 취해야 할 행동, 119·중독정보센터(1339) 신고 방법 안내',
     'https://www.youtube.com/watch?v=gR6Y2_ZQi4Y', 'gR6Y2_ZQi4Y',
     'https://img.youtube.com/vi/gR6Y2_ZQi4Y/hqdefault.jpg', '4:41', 0, 8, true, 2, NOW(), NOW());


-- ============================================
-- 약물 상호작용 영상 (category_id = 3)
-- ============================================
INSERT INTO prevention_videos
(title, description, youtube_url, youtube_id, thumbnail_url, duration, view_count, display_order, is_active, category_id, created_at, updated_at)
VALUES
    ('이 약이랑 저 약 같이 먹어도 돼? - 약물 상호작용 기초',
     '두 가지 이상의 약을 동시에 복용할 때 발생하는 상호작용의 기본 개념과 주의사항 안내',
     'https://www.youtube.com/watch?v=Jg2TpWHvMoA', 'Jg2TpWHvMoA',
     'https://img.youtube.com/vi/Jg2TpWHvMoA/hqdefault.jpg', '5:20', 0, 1, true, 3, NOW(), NOW()),

    ('감기약 + 두통약 같이 먹으면? - 성분 중복 주의!',
     '시중 감기약과 두통약에 동일 성분(아세트아미노펜 등)이 겹쳐 과다복용되는 사례와 예방법',
     'https://www.youtube.com/watch?v=X1vHGJgL0vI', 'X1vHGJgL0vI',
     'https://img.youtube.com/vi/X1vHGJgL0vI/hqdefault.jpg', '4:37', 0, 2, true, 3, NOW(), NOW()),

    ('칼슘제와 철분제 같이 먹으면 안 되는 이유',
     '칼슘제와 철분제를 동시에 복용하면 흡수를 서로 방해하는 이유와 올바른 복용 간격 안내',
     'https://www.youtube.com/watch?v=KJKKqvZd1Cs', 'KJKKqvZd1Cs',
     'https://img.youtube.com/vi/KJKKqvZd1Cs/hqdefault.jpg', '4:00', 0, 3, true, 3, NOW(), NOW()),

    ('자몽 주스가 약을 위험하게 만든다? - 음식-약물 상호작용',
     '자몽 주스가 특정 약물의 혈중 농도를 높여 부작용을 일으키는 원리와 피해야 할 약 목록',
     'https://www.youtube.com/watch?v=JgbfVNBfXHI', 'JgbfVNBfXHI',
     'https://img.youtube.com/vi/JgbfVNBfXHI/hqdefault.jpg', '5:47', 0, 4, true, 3, NOW(), NOW()),

    ('항생제 복용 중 유산균 먹어도 될까?',
     '항생제 복용 중 유산균(프로바이오틱스) 섭취 시 주의할 점과 올바른 복용 간격 안내',
     'https://www.youtube.com/watch?v=e3I0Pq4qUaU', 'e3I0Pq4qUaU',
     'https://img.youtube.com/vi/e3I0Pq4qUaU/hqdefault.jpg', '4:52', 0, 5, true, 3, NOW(), NOW()),

    ('건강기능식품과 처방약, 함께 먹어도 될까?',
     '오메가3·홍삼·비타민 등 건강기능식품과 처방약의 상호작용 사례와 의사·약사 상담의 중요성',
     'https://www.youtube.com/watch?v=y0b2BxNFYL0', 'y0b2BxNFYL0',
     'https://img.youtube.com/vi/y0b2BxNFYL0/hqdefault.jpg', '5:10', 0, 6, true, 3, NOW(), NOW()),

    ('소화제와 항생제 동시 복용하면 안 되는 이유',
     '소화제가 항생제의 흡수율을 낮추는 원리와 복용 간격을 두어야 하는 이유 설명',
     'https://www.youtube.com/watch?v=fXkHC6tGrKU', 'fXkHC6tGrKU',
     'https://img.youtube.com/vi/fXkHC6tGrKU/hqdefault.jpg', '3:58', 0, 7, true, 3, NOW(), NOW()),

    ('여러 약 함께 먹는 노인·만성질환자 주의사항',
     '다약제 복용(Polypharmacy)의 위험성과 복용 중인 약 목록을 항상 지참해야 하는 이유 설명',
     'https://www.youtube.com/watch?v=jsKMno7Lfo8', 'jsKMno7Lfo8',
     'https://img.youtube.com/vi/jsKMno7Lfo8/hqdefault.jpg', '5:00', 0, 8, true, 3, NOW(), NOW());


-- ============================================
-- 1인 가구 복약 관리 영상 (category_id = 4)
-- ============================================
INSERT INTO prevention_videos
(title, description, youtube_url, youtube_id, thumbnail_url, duration, view_count, display_order, is_active, category_id, created_at, updated_at)
VALUES
    ('혼자 사는 당신의 건강, 복약 관리부터 시작하세요!',
     '1인 가구가 스스로 복약 관리를 철저히 해야 하는 이유와 기본적인 건강 관리 팁을 제공하는 영상',
     'https://www.youtube.com/watch?v=f1rjXFCyvlg', 'f1rjXFCyvlg',
     'https://img.youtube.com/vi/f1rjXFCyvlg/hqdefault.jpg', '5:30', 0, 1, true, 4, NOW(), NOW()),

    ('스마트 복약함으로 잊지 않고 약 챙겨 먹기',
     '1인 가구를 위한 스마트 복약함 등 보조 도구를 활용하여 복용 시간을 놓치지 않는 방법 소개',
     'https://www.youtube.com/watch?v=mK0gc4QoU38', 'mK0gc4QoU38',
     'https://img.youtube.com/vi/mK0gc4QoU38/hqdefault.jpg', '4:15', 0, 2, true, 4, NOW(), NOW()),

    ('혼자 아플 때 당황하지 마세요 - 1인 가구 비상약 상비법',
     '1인 가구가 갖춰야 할 필수 비상약 목록과 각 약의 효과·보관법 안내로 응급 상황 대비',
     'https://www.youtube.com/watch?v=hcIKY4nD9N8', 'hcIKY4nD9N8',
     'https://img.youtube.com/vi/hcIKY4nD9N8/hqdefault.jpg', '4:50', 0, 3, true, 4, NOW(), NOW()),

    ('1인 가구 건강 관리의 중요성 - 혼자 사는 당신의 건강은?',
     '1인 가구의 건강 취약성을 설명하고, 복약 관리의 중요성을 강조하며 꾸준한 건강 관리를 독려하는 영상',
     'https://www.youtube.com/watch?v=KR3e2lZDm30', 'KR3e2lZDm30',
     'https://img.youtube.com/vi/KR3e2lZDm30/hqdefault.jpg', '5:00', 0, 4, true, 4, NOW(), NOW()),

    ('어르신을 위한 올바른 복약 길잡이',
     '나이가 들수록 약물 부작용이 잦아지는 이유와 여러 가지 약을 복용하는 어르신을 위한 안전한 복약 방법 안내',
     'https://www.youtube.com/watch?v=T8wZTg1Sf5Y', 'T8wZTg1Sf5Y',
     'https://img.youtube.com/vi/T8wZTg1Sf5Y/hqdefault.jpg', '6:00', 0, 5, true, 4, NOW(), NOW()),

    ('집에 있는 비상약, 제대로 쓰고 있나요? - 올바른 약 사용법',
     '집에 상비되어 있는 비상약들의 유효기간·보관법·사용법을 점검하고 올바른 약 사용 습관을 알려주는 영상',
     'https://www.youtube.com/watch?v=vXubjpbCWcs', 'vXubjpbCWcs',
     'https://img.youtube.com/vi/vXubjpbCWcs/hqdefault.jpg', '5:22', 0, 6, true, 4, NOW(), NOW()),

    ('약 정리 가이드 - 우리 집 약장을 정리해봐요!',
     '집에 흩어져 있는 약들을 효율적으로 정리하고 관리하는 방법을 알려 복약 실수를 줄이는 데 도움을 주는 영상',
     'https://www.youtube.com/watch?v=qvH1dkGGWVE', 'qvH1dkGGWVE',
     'https://img.youtube.com/vi/qvH1dkGGWVE/hqdefault.jpg', '4:10', 0, 7, true, 4, NOW(), NOW()),

    ('복약 게이미피케이션 - 약 챙기는 게 게임처럼 재밌어진다!',
     '포인트·뱃지·리워드 시스템을 활용해 꾸준한 복약 습관을 재미있게 만드는 방법과 앱 활용 사례 소개',
     'https://www.youtube.com/watch?v=5nGMXXeQhMw', '5nGMXXeQhMw',
     'https://img.youtube.com/vi/5nGMXXeQhMw/hqdefault.jpg', '4:45', 0, 8, true, 4, NOW(), NOW());
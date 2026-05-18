-- ============================================
-- MediGuard AI - 복약 안전 교육 영상 데이터 (26.05 검증 완료)
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
                                                                                                                 (4, '1인 가구 상비약',    '자취생을 위한 필수 상비약 관리 영상',   'home_health',     4, true, NOW(), NOW());

-- ============================================
-- 1. 올바른 복약법 영상 (category_id = 1)
-- ============================================
INSERT INTO prevention_videos
(title, description, youtube_url, youtube_id, thumbnail_url, duration, view_count, display_order, is_active, category_id, created_at, updated_at)
VALUES
    ('올바른 약 복용법 #의약품 #복용법 [KBS 방송]',
     'KBS 대전에서 방영된 101세의 프러포즈 - 올바른 약 복용법을 상세히 알려주는 가이드 영상입니다.',
     'https://www.youtube.com/watch?v=XT0cBbORaMk', 'XT0cBbORaMk',
     'https://i.ytimg.com/vi/XT0cBbORaMk/maxresdefault.jpg', '22:41', 5645, 1, true, 1, NOW(), NOW()),

    ('약 이렇게 먹어도 되나요? 올바른 약 복용법 3가지',
     '서울대병원 전문의가 알려주는 식후 30분 복용의 진짜 이유와 물과 함께 마셔야 하는 이유를 팩트체크합니다.',
     'https://www.youtube.com/watch?v=XqaDzEfwfoc', 'XqaDzEfwfoc',
     'https://i.ytimg.com/vi/XqaDzEfwfoc/maxresdefault.jpg', '05:12', 8355, 2, true, 1, NOW(), NOW()),

    ('약 복용 시 주의해야할 사항은?',
     '인제대학교 백병원에서 제작한 영상으로, 질환 치료를 위한 올바른 약 복용 수칙을 짧고 굵게 설명합니다.',
     'https://www.youtube.com/watch?v=94Q2Vh1x-WE', '94Q2Vh1x-WE',
     'https://i.ytimg.com/vi/94Q2Vh1x-WE/maxresdefault.jpg', '03:08', 793, 3, true, 1, NOW(), NOW()),

    ('[복약지도] 어르신들을 위한 올바른 약 복용법',
     '독거노인종합지원센터에서 제공하는 알기 쉬운 약 복용 가이드와 일상생활 속 주의사항 모음입니다.',
     'https://www.youtube.com/watch?v=Mz_UIgyDGnc', 'Mz_UIgyDGnc',
     'https://i.ytimg.com/vi/Mz_UIgyDGnc/maxresdefault.jpg', '11:03', 20163, 4, true, 1, NOW(), NOW());

-- ============================================
-- 2. 약 부작용 & 주의사항 영상 (category_id = 2)
-- ============================================
INSERT INTO prevention_videos
(title, description, youtube_url, youtube_id, thumbnail_url, duration, view_count, display_order, is_active, category_id, created_at, updated_at)
VALUES
    ('이런 증상이 나타나면 고지혈증약 부작용을 의심해보세요',
     '의학채널 비온뒤에서 소개하는 약 복용 후 흔히 겪는 근육통 등 스타틴 계열의 대표적 부작용 설명 영상입니다.',
     'https://www.youtube.com/watch?v=MK439-xiNTU', 'MK439-xiNTU',
     'https://i.ytimg.com/vi/MK439-xiNTU/maxresdefault.jpg', '06:00', 1552002, 1, true, 2, NOW(), NOW()),

    ('반드시 먹어야 하는 부정맥약 부작용',
     '서울아산병원 심장내과 전문의가 직접 알려주는 항부정맥제의 효과와 간과하기 쉬운 부작용 증상 안내입니다.',
     'https://www.youtube.com/watch?v=owA5emjxuJU', 'owA5emjxuJU',
     'https://i.ytimg.com/vi/owA5emjxuJU/maxresdefault.jpg', '09:00', 23152, 2, true, 2, NOW(), NOW()),

    ('고령층의 약물 부작용 주의사항 [올바른 약물 복용법]',
     '대구MBC 뉴스에서 취재한 신체 기능이 떨어진 고령층이 약을 복용할 때 특히 주의해야 할 부작용 사례들입니다.',
     'https://www.youtube.com/watch?v=a0dNfKlJc-g', 'a0dNfKlJc-g',
     'https://i.ytimg.com/vi/a0dNfKlJc-g/maxresdefault.jpg', '04:59', 101, 3, true, 2, NOW(), NOW()),

    ('약 부작용이 심한데 중단해도 되나요?',
     '양병원 소화기내과에서 알려주는 헬리코박터균 약 부작용 발생 시 올바른 대처법과 임의 중단의 위험성입니다.',
     'https://www.youtube.com/watch?v=POIkEskGCAE', 'POIkEskGCAE',
     'https://i.ytimg.com/vi/POIkEskGCAE/maxresdefault.jpg', '04:36', 2913, 4, true, 2, NOW(), NOW());

-- ============================================
-- 3. 약물 상호작용 영상 (category_id = 3)
-- ============================================
INSERT INTO prevention_videos
(title, description, youtube_url, youtube_id, thumbnail_url, duration, view_count, display_order, is_active, category_id, created_at, updated_at)
VALUES
    ('약과 약, 약물상호작용에 주의하세요!',
     '식품의약품안전처에서 공식 제작한 영상으로, 여러 약을 한 번에 먹을 때 주의해야 할 조합을 알기 쉽게 풀어줍니다.',
     'https://www.youtube.com/watch?v=Ym1tNoV1GFs', 'Ym1tNoV1GFs',
     'https://i.ytimg.com/vi/Ym1tNoV1GFs/maxresdefault.jpg', '09:25', 16223, 1, true, 3, NOW(), NOW()),

    ('이 약 복용시 자몽 함께 먹으면 큰일 납니다!',
     '음식과 약의 상호작용에 대해 설명하며, 특히 자몽 주스와 함께 먹으면 위험한 약물들을 콕 집어 알려드립니다.',
     'https://www.youtube.com/watch?v=EkiqedhQS24', 'EkiqedhQS24',
     'https://i.ytimg.com/vi/EkiqedhQS24/maxresdefault.jpg', '05:29', 19104, 2, true, 3, NOW(), NOW()),

    ('마그네슘 "이것"과 같이 먹으면 전부 헛수고가 됩니다',
     '리틀약사 채널에서 소개하는 영양제(마그네슘)와 다른 약물의 상호작용 및 흡수율을 떨어뜨리는 잘못된 조합입니다.',
     'https://www.youtube.com/watch?v=RYU6ayN23Vw', 'RYU6ayN23Vw',
     'https://i.ytimg.com/vi/RYU6ayN23Vw/maxresdefault.jpg', '04:32', 132469, 3, true, 3, NOW(), NOW()),

    ('고혈압약 먹는다면 주의해야 하는 영양제?',
     '가천대 길병원 가정의학과 교수가 설명하는 만성질환 약물(고혈압약)과 시중 영양제 간의 상호작용 주의사항입니다.',
     'https://www.youtube.com/watch?v=-EK-51SItd0', '-EK-51SItd0',
     'https://i.ytimg.com/vi/-EK-51SItd0/maxresdefault.jpg', '01:24', 58617, 4, true, 3, NOW(), NOW());

-- ============================================
-- 4. 1인 가구 복약 관리 영상 (category_id = 4)
-- ============================================
INSERT INTO prevention_videos
(title, description, youtube_url, youtube_id, thumbnail_url, duration, view_count, display_order, is_active, category_id, created_at, updated_at)
VALUES
    ('3만원으로 해결하는 자취생 필수 상비약 7종',
     '약국 문이 닫힌 주말이나 밤늦게 아플 때를 대비해, 1인 가구가 미리 구비해 두어야 할 상비약 목록을 총정리합니다.',
     'https://www.youtube.com/watch?v=sKuydl_o-As', 'sKuydl_o-As',
     'https://i.ytimg.com/vi/sKuydl_o-As/maxresdefault.jpg', '06:38', 1145, 1, true, 4, NOW(), NOW()),

    ('구급상자에 없으면 불안한 우리집 상비약 3가지 추천!',
     '자취생 구급상자에 꼭 들어가 있어야 할 필수 일반 의약품 3가지를 짧고 명확하게 추천하는 쇼츠 영상입니다.',
     'https://www.youtube.com/watch?v=sIkBAdpcExk', 'sIkBAdpcExk',
     'https://i.ytimg.com/vi/sIkBAdpcExk/maxresdefault.jpg', '00:22', 237360, 2, true, 4, NOW(), NOW());
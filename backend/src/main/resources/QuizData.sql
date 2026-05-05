-- ============================================
-- MediGuard AI - 퀴즈 카테고리 데이터
-- ============================================
INSERT INTO quiz_category (category_id, category_name, description, icon, display_order, is_active, created_at, updated_at) VALUES
                                                                                                                                (1, '기초 복약 상식', '약 복용의 기본 개념과 상식', 'medication', 1, true, NOW(), NOW()),
                                                                                                                                (2, '약 부작용 & 주의사항', '약 부작용 종류와 대처법', 'warning_amber', 2, true, NOW(), NOW()),
                                                                                                                                (3, '올바른 복용법', '약을 올바르게 복용하는 방법', 'health_and_safety', 3, true, NOW(), NOW()),
                                                                                                                                (4, '약물 상호작용', '약과 음식, 약과 약의 상호작용', 'compare_arrows', 4, true, NOW(), NOW());


-- ============================================
-- 기초 복약 상식 (category_id = 1) - level 1
-- ============================================
INSERT INTO quiz (quiz_id, category_id, question, level, answer, point) VALUES
                                                                            ( quiz_id 1001, category_id 1, question '약을 복용할 때 가장 권장되는 음료는?',                              level 1, answer '물',                    point 10),
                                                                            ( quiz_id 1002, category_id 1, question '처방받은 약은 증상이 나아도 어떻게 해야 하는가?',                   level 1, answer '처방대로 끝까지 복용',     point 10),
                                                                            ( quiz_id 1003, category_id 1, question '약을 안전하게 보관하기에 적절하지 않은 장소는?',                    level 1, answer '욕실',                  point 10),
                                                                            ( quiz_id 1004, category_id 1, question '유효기간이 지난 약은 어떻게 처리해야 하는가?',                      level 1, answer '약국에 반납',             point 10),
                                                                            ( quiz_id 1005, category_id 1, question '처방전 없이 구입할 수 있는 약을 무엇이라 하는가?',                  level 1, answer '일반의약품',              point 10),
                                                                            ( quiz_id 1006, category_id 1, question '약 봉투에 적힌 복약 지도를 따르지 않으면 어떤 문제가 생길 수 있나?', level 1, answer '약효 저하 또는 부작용',    point 10),
                                                                            ( quiz_id 1007, category_id 1, question '냉장 보관이 필요한 약을 상온에 두면 어떻게 되는가?',                level 1, answer '약효가 떨어질 수 있다',   point 10),
                                                                            ( quiz_id 1008, category_id 1, question '약사나 의사의 지시 없이 다른 사람의 처방약을 복용하면?',             level 1, answer '부작용이 발생할 수 있다', point 10),
                                                                            ( quiz_id 1009, category_id 1, question '복용을 잊었을 때 다음 복용 시간이 가까우면 어떻게 해야 하는가?',     level 1, answer '한 번 건너뛰고 다음 시간에 복용', point 10),
                                                                            ( quiz_id 1010, category_id 1, question '약을 쪼개거나 가루로 만들면 안 되는 약 종류는?',                    level 1, answer '서방형 제제',             point 10);


-- ============================================
-- 기초 복약 상식 (category_id = 1) - level 2
-- ============================================
INSERT INTO quiz (quiz_id, category_id, question, level, answer, point) VALUES
                                                                            ( quiz_id 1011, category_id 1, question '식전 복용 약을 식사 몇 분 전에 먹는 것이 일반적인가?',              level 2, answer '30분 전',               point 20),
                                                                            ( quiz_id 1012, category_id 1, question '항생제를 임의로 중단하면 발생할 수 있는 문제는?',                   level 2, answer '내성균 발생',             point 20),
                                                                            ( quiz_id 1013, category_id 1, question '점안액(안약) 두 종류를 사용할 때 권장 간격은?',                     level 2, answer '5분 이상',               point 20),
                                                                            ( quiz_id 1014, category_id 1, question '알약의 SR, XR 표시는 무엇을 뜻하는가?',                            level 2, answer '서방형 제제',             point 20),
                                                                            ( quiz_id 1015, category_id 1, question '어린이 약 용량 기준은 무엇으로 결정되는가?',                        level 2, answer '체중',                  point 20);


-- ============================================
-- 약 부작용 & 주의사항 (category_id = 2) - level 1
-- ============================================
INSERT INTO quiz (quiz_id, category_id, question, level, answer, point) VALUES
                                                                            ( quiz_id 2001, category_id 2, question '아세트아미노펜을 과다복용하면 가장 크게 손상되는 장기는?',           level 1, answer '간',                    point 10),
                                                                            ( quiz_id 2002, category_id 2, question '소염진통제를 공복에 복용하면 어느 부위가 손상될 수 있는가?',         level 1, answer '위 점막',               point 10),
                                                                            ( quiz_id 2003, category_id 2, question '항히스타민제 복용 후 특히 주의해야 할 활동은?',                     level 1, answer '운전',                  point 10),
                                                                            ( quiz_id 2004, category_id 2, question '약 복용 후 두드러기와 호흡곤란이 생기면 어떻게 해야 하는가?',        level 1, answer '즉시 119에 연락',         point 10),
                                                                            ( quiz_id 2005, category_id 2, question '약물 부작용을 신고할 수 있는 기관은?',                              level 1, answer '한국의약품안전관리원',     point 10),
                                                                            ( quiz_id 2006, category_id 2, question '혈압약을 증상이 없다고 임의로 중단하면 어떤 위험이 있는가?',         level 1, answer '혈압이 갑자기 올라 위험할 수 있다', point 10),
                                                                            ( quiz_id 2007, category_id 2, question '알코올과 수면제를 함께 복용하면 어떤 위험이 있는가?',               level 1, answer '호흡 억제 및 과진정',     point 10),
                                                                            ( quiz_id 2008, category_id 2, question '약 부작용 신고 전화번호는?',                                        level 1, answer '1644-6223',             point 10),
                                                                            ( quiz_id 2009, category_id 2, question '약물 과다복용 응급 시 연락할 중독정보센터 번호는?',                  level 1, answer '1339',                  point 10),
                                                                            ( quiz_id 2010, category_id 2, question '당뇨약을 임의로 중단하면 발생할 수 있는 위험은?',                   level 1, answer '혈당이 급격히 오를 수 있다', point 10);


-- ============================================
-- 약 부작용 & 주의사항 (category_id = 2) - level 2
-- ============================================
INSERT INTO quiz (quiz_id, category_id, question, level, answer, point) VALUES
                                                                            ( quiz_id 2011, category_id 2, question '스테로이드제를 장기 복용 시 나타날 수 있는 부작용은?',               level 2, answer '면역력 저하 및 골다공증', point 20),
                                                                            ( quiz_id 2012, category_id 2, question '임신 중 복용을 반드시 피해야 하는 약 성분은?',                      level 2, answer '이소트레티노인',           point 20),
                                                                            ( quiz_id 2013, category_id 2, question '항응고제 복용 중 출혈이 생기면 어떻게 해야 하는가?',                 level 2, answer '즉시 의사에게 알린다',    point 20),
                                                                            ( quiz_id 2014, category_id 2, question '타이레놀 성인 1일 최대 복용량은?',                                  level 2, answer '4,000mg',               point 20),
                                                                            ( quiz_id 2015, category_id 2, question '항생제 부작용으로 가장 흔하게 나타나는 증상은?',                     level 2, answer '설사 및 소화 장애',       point 20);


-- ============================================
-- 올바른 복용법 (category_id = 3) - level 1
-- ============================================
INSERT INTO quiz (quiz_id, category_id, question, level, answer, point) VALUES
                                                                            ( quiz_id 3001, category_id 3, question '식후 복용 약은 식사 후 몇 분 이내에 복용하는 것이 좋은가?',          level 1, answer '30분 이내',              point 10),
                                                                            ( quiz_id 3002, category_id 3, question '캡슐 약을 열어서 가루만 먹으면 안 되는 이유는?',                    level 1, answer '약효 및 흡수에 영향을 주기 때문', point 10),
                                                                            ( quiz_id 3003, category_id 3, question '복약 알림을 설정하기 가장 좋은 방법은?',                             level 1, answer '스마트폰 알람 또는 복약 알림 앱', point 10),
                                                                            ( quiz_id 3004, category_id 3, question '약을 올바르게 보관하는 장소로 가장 적절한 곳은?',                    level 1, answer '서늘하고 건조한 장소',    point 10),
                                                                            ( quiz_id 3005, category_id 3, question '취침 전 복용 약을 잠자기 몇 분 전에 먹는 것이 좋은가?',              level 1, answer '30분 전',               point 10),
                                                                            ( quiz_id 3006, category_id 3, question '여러 가지 약을 복용할 때 꼭 챙겨야 할 기록은?',                     level 1, answer '복약 일지',              point 10),
                                                                            ( quiz_id 3007, category_id 3, question '약 봉투를 버릴 때 반드시 확인하고 남겨야 할 정보는?',               level 1, answer '복약 정보 및 유효기간',   point 10),
                                                                            ( quiz_id 3008, category_id 3, question '물 없이 약을 삼키면 생길 수 있는 문제는?',                          level 1, answer '식도에 약이 달라붙어 손상될 수 있다', point 10),
                                                                            ( quiz_id 3009, category_id 3, question '가루약을 복용할 때 어떻게 먹는 것이 좋은가?',                       level 1, answer '물에 타서 복용',          point 10),
                                                                            ( quiz_id 3010, category_id 3, question '복약 지도를 받기 가장 좋은 사람은 누구인가?',                       level 1, answer '약사',                  point 10);


-- ============================================
-- 올바른 복용법 (category_id = 3) - level 2
-- ============================================
INSERT INTO quiz (quiz_id, category_id, question, level, answer, point) VALUES
                                                                            ( quiz_id 3011, category_id 3, question '두 가지 이상의 안약을 사용할 때 올바른 순서는?',                     level 2, answer '점도 낮은 것 먼저, 간격 5분 이상', point 20),
                                                                            ( quiz_id 3012, category_id 3, question '철분제 복용 시 흡수를 높이기 위해 함께 먹으면 좋은 것은?',           level 2, answer '비타민 C',               point 20),
                                                                            ( quiz_id 3013, category_id 3, question '패치형 약을 붙이기 가장 적합한 부위는?',                             level 2, answer '털이 없고 평평한 피부',   point 20),
                                                                            ( quiz_id 3014, category_id 3, question '흡입기(천식약)를 올바르게 사용하려면 흡입 후 무엇을 해야 하는가?',   level 2, answer '숨을 10초간 참는다',      point 20),
                                                                            ( quiz_id 3015, category_id 3, question '냉장 보관 약을 복용 전 어떻게 해야 하는가?',                        level 2, answer '잠시 실온에 두어 온도를 맞춘다', point 20);


-- ============================================
-- 약물 상호작용 (category_id = 4) - level 1
-- ============================================
INSERT INTO quiz (quiz_id, category_id, question, level, answer, point) VALUES
                                                                            ( quiz_id 4001, category_id 4, question '약과 함께 마시면 안 되는 음료는?',                                  level 1, answer '자몽 주스',              point 10),
                                                                            ( quiz_id 4002, category_id 4, question '칼슘제와 철분제를 함께 복용하면 안 되는 이유는?',                   level 1, answer '서로 흡수를 방해하기 때문', point 10),
                                                                            ( quiz_id 4003, category_id 4, question '술과 항생제를 함께 복용하면 어떤 반응이 생길 수 있는가?',            level 1, answer '구역, 구토, 두통 등 심한 반응', point 10),
                                                                            ( quiz_id 4004, category_id 4, question '감기약과 두통약을 함께 먹을 때 주의해야 할 성분은?',                 level 1, answer '아세트아미노펜 성분 중복', point 10),
                                                                            ( quiz_id 4005, category_id 4, question '건강기능식품도 처방약과 상호작용을 일으킬 수 있는가?',               level 1, answer '그렇다',                 point 10),
                                                                            ( quiz_id 4006, category_id 4, question '항생제 복용 중 유산균은 몇 시간 간격을 두고 먹는 것이 좋은가?',      level 1, answer '2시간 이상',             point 10),
                                                                            ( quiz_id 4007, category_id 4, question '소화제를 항생제와 함께 복용하면 안 되는 이유는?',                   level 1, answer '항생제 흡수율을 낮추기 때문', point 10),
                                                                            ( quiz_id 4008, category_id 4, question '여러 약을 복용할 때 의사·약사에게 알려야 할 정보는?',               level 1, answer '복용 중인 모든 약 목록', point 10),
                                                                            ( quiz_id 4009, category_id 4, question '자몽 주스가 특정 약을 위험하게 만드는 이유는?',                     level 1, answer '약물의 혈중 농도를 높이기 때문', point 10),
                                                                            ( quiz_id 4010, category_id 4, question '홍삼이 상호작용을 일으킬 수 있는 약은?',                            level 1, answer '항응고제(와파린)',         point 10);


-- ============================================
-- 약물 상호작용 (category_id = 4) - level 2
-- ============================================
INSERT INTO quiz (quiz_id, category_id, question, level, answer, point) VALUES
                                                                            ( quiz_id 4011, category_id 4, question '우유와 함께 복용하면 흡수가 방해되는 약은?',                        level 2, answer '테트라사이클린 계열 항생제', point 20),
                                                                            ( quiz_id 4012, category_id 4, question '다약제 복용(Polypharmacy)의 기준은 몇 가지 이상의 약 복용인가?',    level 2, answer '5가지 이상',             point 20),
                                                                            ( quiz_id 4013, category_id 4, question '오메가3와 함께 복용 시 출혈 위험이 높아지는 약은?',                 level 2, answer '항응고제',               point 20),
                                                                            ( quiz_id 4014, category_id 4, question '갑상선 약을 복용할 때 함께 먹으면 흡수가 방해되는 것은?',           level 2, answer '칼슘제 및 철분제',        point 20),
                                                                            ( quiz_id 4015, category_id 4, question 'MAO 억제제 복용 중 피해야 할 음식은?',                              level 2, answer '치즈, 와인 등 티라민 함유 식품', point 20);

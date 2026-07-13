SET NAMES utf8mb4;
USE travel_platform;

INSERT INTO `user` (`id`, `username`, `password`, `nickname`, `phone`, `email`, `avatar`, `gender`, `status`, `created_at`, `updated_at`) VALUES
(1, 'travel_lily', '$2b$12$ZnfUP.rDvDysdedgQinD9uoTRSTkf1QsPsirGVX4JuJ2l9dzrset2', '莉莉旅行记', '13800001001', 'lily@qq.com', 'https://i.pravatar.cc/150?img=1', 2, 1, '2025-06-01 10:00:00', '2025-06-01 10:00:00'),
(2, 'jack_road', '$2b$12$xkyPEBLMkYNa/QoCOV5y1u/ZQfthnUyhFZ2akaQN6wxk37miYZWju', '行走的杰克', '13800001002', 'jack@qq.com', 'https://i.pravatar.cc/150?img=2', 1, 1, '2025-06-02 11:00:00', '2025-06-02 11:00:00'),
(3, 'sunny_ma', '$2b$12$rg4YIEy0ouYLS91Lmbs5vO/r3cWty4eryHFShZr8TfCvV0wiWNC..', '马小阳', '13800001003', 'sunny@qq.com', 'https://i.pravatar.cc/150?img=3', 2, 1, '2025-06-03 09:30:00', '2025-06-03 09:30:00'),
(4, 'ai_fan', '$2b$12$RyiVktYuj6H2KPavRGEIRu0ZBnp2WV0LuEULQirM8F.EsC7utkJfC', 'AI旅游家', '13800001004', 'aifan@qq.com', 'https://i.pravatar.cc/150?img=4', 1, 0, '2025-06-04 14:20:00', '2025-06-04 14:20:00'),
(5, 'foodie_wang', '$2b$12$OVwno7u1Oqn849gO925Nhehi5RQWt224TsDKuNp5rPkTSMKQVcCF.', '美食猎人王', '13800001005', 'foodie@qq.com', 'https://i.pravatar.cc/150?img=5', 1, 1, '2025-06-05 16:45:00', '2025-06-05 16:45:00'),
(6, 'linda_zhou', '$2b$12$OLsmLS1OldMfuG2gn5OkhOXzBmGgNlMAo8G8o6XyFdvhW1nWRGO7O', '琳达看世界', '13800001006', 'linda@qq.com', 'https://i.pravatar.cc/150?img=6', 2, 1, '2025-06-06 08:15:00', '2025-06-06 08:15:00'),
(7, 'dad_li', '$2b$12$9N31RRI7a8eHVt8seFU0L.x3G1AolXbPPqh7oXNAx1bWs26QCWlPS', '带娃旅行李', '13800001007', 'dali@qq.com', 'https://i.pravatar.cc/150?img=7', 1, 1, '2025-06-07 12:30:00', '2025-06-07 12:30:00'),
(8, 'photo_chen', '$2b$12$DxGEG1ZCZEbymQfbNghybe5DJZlIVC.9cDg25vfZXJXMdjp.JU2Bm', '镜头里的陈', '13800001008', 'chen@qq.com', 'https://i.pravatar.cc/150?img=8', 1, 1, '2025-06-08 19:00:00', '2025-06-08 19:00:00'),
(9, 'vivian_xu', '$2b$12$LKMmkjS46UFq9viyZ4mfheXOmhGX2tbpO9/5zsRVvlgJzrUdipd22', '徐薇薇安', '13800001009', 'vivian@qq.com', 'https://i.pravatar.cc/150?img=9', 2, 1, '2025-06-09 07:20:00', '2025-06-09 07:20:00'),
(10, 'budget_king', '$2b$12$3/8qqWfR/VltJCII7FY7FOVVqQT8gSNnyOGwT844/L.AL6wI5hJrC', '穷游王', '13800001010', 'budget@qq.com', 'https://i.pravatar.cc/150?img=10', 1, 1, '2025-06-10 13:50:00', '2025-06-10 13:50:00'),
(11, 'sea_lover', '$2b$12$S1AKGgi.W5UkhMlJve2D4.aC0sVWO/zTFJxx5KMXN.MHm95iL0Xz6', '大海控', '13800001011', 'sea@qq.com', 'https://i.pravatar.cc/150?img=11', 2, 1, '2025-06-11 15:40:00', '2025-06-11 15:40:00'),
(12, 'hist_fan', '$2b$12$WjKkdBa2EIhw.w0ERNumueAbkOsGqWGMgsmSkj53au.moGxey407O', '历史迷小赵', '13800001012', 'hist@qq.com', 'https://i.pravatar.cc/150?img=12', 1, 0, '2025-06-12 10:10:00', '2025-06-12 10:10:00'),
(13, 'night_owl', '$2b$12$f6IY2q8IFaBkigqWBZknDutzBxr5JnsnBBFGor2p8pHEfXkxAfgeK', '夜游神', '13800001013', 'night@qq.com', 'https://i.pravatar.cc/150?img=13', 2, 1, '2025-06-13 22:30:00', '2025-06-13 22:30:00'),
(14, 'couple_lin', '$2b$12$5WnYn4QApvXeoXcUJA6Ynu0HHGI8Hia3Dgu32AR5G1.AWErMvaw6q', '林氏夫妇游', '13800001014', 'lin@qq.com', 'https://i.pravatar.cc/150?img=14', 1, 1, '2025-06-14 09:00:00', '2025-06-14 09:00:00'),
(15, 'solo_zhang', '$2b$12$V5pp1N6NiFhM6mMJarcvouaiMrR7RRAcoAdSir2wHjDKnImU2Owd.', '独行张', '13800001015', 'solo@qq.com', 'https://i.pravatar.cc/150?img=15', 1, 1, '2025-06-15 17:25:00', '2025-06-15 17:25:00'),
(16, 'may_huang', '$2b$12$3KhHijDSPPSPZpScffIuz.iGDhptSrT8r/J3ibQbIP6V8wxgP2H9O', '五月黄', '13800001016', 'may@qq.com', 'https://i.pravatar.cc/150?img=16', 2, 1, '2025-06-16 11:55:00', '2025-06-16 11:55:00'),
(17, 'winter_liu', '$2b$12$77wGLE5uq/Y3Jjsi9WEmY.lzCUdV0lSWL.fZ.OTGQye.qmlcFnL.S', '冬日刘', '13800001017', 'winter@qq.com', 'https://i.pravatar.cc/150?img=17', 1, 1, '2025-06-17 06:35:00', '2025-06-17 06:35:00'),
(18, 'tea_yang', '$2b$12$tzGsHyATN5cLsjaigmUtX.yHmkgJhacZn2WE8OurBSDFFjfGMiVB6', '品茶杨', '13800001018', 'tea@qq.com', 'https://i.pravatar.cc/150?img=18', 2, 0, '2025-06-18 14:05:00', '2025-06-18 14:05:00'),
(19, 'rush_li', '$2b$12$mMfIHrgks7QBwrS2i2vZ7.9zn4.8FHUL1YOZN.ammPEN5snVJjQki', '李不急', '13800001019', 'rush@qq.com', 'https://i.pravatar.cc/150?img=19', 1, 1, '2025-06-19 08:45:00', '2025-06-19 08:45:00'),
(20, 'dream_chen', '$2b$12$q5ayJCaBJnHDOiskmcyiI.JlZfPg5TAR.TxqfwvHo9YotVlm8gJsW', '追梦陈', '13800001020', 'dream@qq.com', 'https://i.pravatar.cc/150?img=20', 2, 1, '2025-06-20 12:15:00', '2025-06-20 12:15:00');

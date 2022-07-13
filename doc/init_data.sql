truncate table lab_out;
truncate table lab_apply;
truncate table lab_join_link;

truncate table base_user;
insert into base_user(id, email, salt, password, username) values
(1, 'lyh@lab.com', 'lyh', sha2('lyhlyh', 256), 'lyh'),
(2, 'zlm@lab.com', 'zlm', sha2('zlmzlm', 256), 'zlm'),
(3, 'jcz@lab.com', 'jcz', sha2('jczjcz', 256), 'jcz'),
(4, 'qpf@lab.com', 'qpf', sha2('qpfqpf', 256), 'qpf'),
(5, 'test@lab.com', 'test', sha2('testtest', 256), 'test');

truncate table lab_perm;
insert into lab_perm(id, name, tag, pid, is_nav) values
(101, '基本操作', 'op', 0, 1),
(102, '实验室管理', 'lab', 0, 1),
(103, '权限管理', 'perm', 0, 1),
(104, '记录单管理', 'record', 0, 1),
(101001, '入库', 'op:in', 101, 0),
(101002, '审批公共申请', 'op:confirm-common-apply', 101, 0),
(101003, '审批购买申请', 'op:confirm-buy-apply', 101, 0),
(101004, '移动架子物品', 'op:move', 101, 0),
(102001, '物品管理', 'lab:item', 102, 1),
(102002, '架子管理', 'lab:shelf', 102, 1),
(103001, '角色管理', 'perm:role', 103, 1),
(103002, '用户管理', 'perm:user', 103, 1),
(104001, '入库记录管理', 'record:in', 104, 1),
(104002, '消耗记录管理', 'record:out', 104, 1),
(104003, '申请/归还记录管理', 'record:apply', 104, 1),
(104004, '购买申请记录管理', 'record:buy-apply', 104, 1);

-- 实验室
truncate table lab;
insert into lab(id, name, belong_base_user_id, create_base_user_id, update_base_user_id) values
(1, '口袋妖怪', 1, 1, 1);

-- 实验室角色
truncate table lab_role;
insert into lab_role(id, name, create_lab_user_id, update_lab_user_id, lab_id) values
(1, '管理员', 1, 1, 1),
(2, '助理员', 1, 1, 1),
(3, '普通成员', 1, 1, 1);
update lab_role set lab_perms = (select group_concat(tag) from lab_perm) where id = 1;
update lab_role set lab_perms = (select group_concat(tag) from lab_perm) where id = 1;
update lab_role set lab_perms = (select group_concat(tag) from lab_perm where id != 104 and pid != 103) where id = 2;

-- 实验室成员
truncate table lab_user;
insert into lab_user(id, lab_id, base_user_id, name, lab_role_id, create_lab_user_id, update_lab_user_id) values
(1, 1, 1, 'lyh', 1, 1, 1),
(2, 1, 2, 'zlm', 1, 1, 1),
(3, 1, 3, 'jcz', 1, 1, 1),
(4, 1, 4, 'qpf', 1, 1, 1);
update lab_user set deleted = 1, delete_time = 1 where id = 4; -- 伪删除


truncate table lab_shelf;
insert into lab_shelf(id, name, type, lab_id, belong_lab_user_id, create_lab_user_id, update_lab_user_id) values
(1, '公共架子1', 1, 1, 0, 1, 1),
(2, '公共架子2', 1, 1, 0, 1, 1),
(3, '贮藏架子3', 2, 1, 0, 1, 1),
(4, 'lyh-架子1', 0, 1, 1, 1, 1),
(5, 'lyh-架子2', 0, 1, 1, 1, 1)
;

-- 非规格品
truncate table lab_item;
insert into lab_item(id, name, eng_name, desc_info, price, unit, floated, capacity, capacity_unit, create_lab_user_id, create_time, update_lab_user_id, update_time, lab_id) values
(1, '妙蛙种子', 'Bulbasaur', 'miaowazhongzi', 1100, '只', 0, 0, '', 1, 0, 1, 0, 1),
(2, '妙蛙草', 'Ivysaur', 'miaowacao', 2200, '只', 0, 0, '', 1, 0, 1, 0, 1),
(3, '妙蛙花', 'Venusaur', 'miaowahua', 3300, '只', 0, 0, '', 1, 0, 1, 0, 1),
(4, '小火龙', 'Charmander', 'xiaohuolong', 1100, '头', 1, 800000, '斤', 1, 0, 1, 0, 1),
(5, '火恐龙', 'Charmeleon', 'huokonglong', 2200, '头', 1, 900000, '斤', 1, 0, 1, 0, 1),
(6, '喷火龙', 'Charizard', 'penhuolong', 3300, '头', 1, 1000000, '斤', 1, 0, 1, 0, 1);

-- 入库
truncate table lab_in;
insert into lab_in(id, lab_item_id, num, price, op_time, op_lab_shelf_id, op_lab_user_id, lab_id) values
( 1, 1, 100000, 1100, 1655301667155, 1, 1, 1),
( 2, 2, 200000, 2200, 1655301667155, 1, 1, 1),
( 3, 3, 300000, 3300, 1655301667155, 1, 1, 1),
( 4, 4, 300000, 1100, 1655301667155, 1, 1, 1),
( 5, 5, 300000, 2200, 1655301667155, 1, 1, 1),
( 6, 6, 300000, 3300, 1655301667155, 1, 1, 1),
( 7, 1, 100000, 1100, 1655301667155, 2, 1, 1),
( 8, 2, 200000, 2200, 1655301667155, 2, 1, 1),
( 9, 3, 300000, 3300, 1655301667155, 2, 1, 1),
(10, 4, 300000, 1100, 1655301667155, 2, 1, 1),
(11, 5, 300000, 2200, 1657525137409, 2, 1, 1),
(12, 6, 300000, 3300, 1657525137409, 2, 1, 1);

-- 库存
truncate table lab_inventory;
insert into lab_inventory(id, lab_shelf_id, lab_in_id, num, lab_id, lab_item_id, capacity) values
(01, 1, 01, 100000, 1, 1, 000),
(02, 1, 02, 200000, 1, 2, 000),
(03, 1, 03, 300000, 1, 3, 000),
(04, 1, 04, 300000, 1, 4, 0800000),
(05, 1, 05, 300000, 1, 5, 0900000),
(06, 1, 06, 300000, 1, 6, 1000000),
(07, 2, 07, 100000, 1, 1, 000),
(08, 2, 08, 200000, 1, 2, 000),
(09, 2, 09, 300000, 1, 3, 000),
(10, 2, 10, 300000, 1, 4, 0800000),
(11, 2, 11, 300000, 1, 5, 0900000),
(12, 2, 12, 300000, 1, 6, 1000000);
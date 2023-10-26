2023年07月18日
1. change table name mall_* to vision_*
2. change class name Mall* to Vision*
3. add table vision_template_files_slot
   ```sql
    CREATE TABLE vision_template_files_slot (
    id INT AUTO_INCREMENT PRIMARY KEY not null,
    sys_files_id VARCHAR(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci not null, -- 设置相同的字符集和排序规则
    vision_video_template_id INT not null,
    name VARCHAR(50) not null,
    remark VARCHAR(100),
    FOREIGN KEY (sys_files_id) REFERENCES sys_files(id),
    FOREIGN KEY (vision_video_template_id) REFERENCES vision_video_template_version(id)
    ) ENGINE=InnoDB; -- 使用 InnoDB 表引擎，支持外键
    ```
4. add fk of files_id for vision_video_template
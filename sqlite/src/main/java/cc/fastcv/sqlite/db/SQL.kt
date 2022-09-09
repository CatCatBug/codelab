package cc.fastcv.sqlite.db

val CREATE_STUDENT_INFO_TABLE = """
            CREATE TABLE student_info(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                student_number VARCHAR(20) NOT NULL,
                name VARCHAR(100) NOT NULL,
                gender INTEGER UNSIGNED NOT NULL,
                phone VARCHAR(11) NOT NULL,
                birthday VARCHAR(100) NOT NULL,
                address VARCHAR(100) NOT NULL
            );
            """.trimIndent()

val CREATE_CLASS_INFO_TABLE = """
            CREATE TABLE class_info(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name VARCHAR(100) NOT NULL
            );
            """.trimIndent()

val CREATE_TEACHER_INFO_TABLE = """
            CREATE TABLE teacher_info(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                teacher_number VARCHAR(20) NOT NULL,
                name VARCHAR(100) NOT NULL,
                gender INTEGER UNSIGNED NOT NULL,
                phone VARCHAR(11) NOT NULL,
                birthday VARCHAR(100) NOT NULL,
                address VARCHAR(100) NOT NULL
            );
            """.trimIndent()


/****  收集的待验证的SQL语句  ****/
//今天
const val SQL_2 = "select * from standing_booktable where operate_time between datetime('now','start of day','+1 seconds') and  datetime('now','start of day','+1 days','-1 seconds')"
//昨天
const val SQL_3 = "select * from standing_booktable where operate_time between datetime('now','start of day','-1 days','+1 seconds') and  datetime('now','start of day','-1 seconds')"
//前天
const val SQL_4 = "select * from standing_booktable where operate_time between datetime('now','start of day','-2 days') and  datetime('now','start of day','-1 days')"
//近七天
const val SQL_5 = "select * from standing_booktable where operate_time between datetime('now','start of day','-6 days') and  datetime('now','start of day','+1 days')"

//按时间分组
const val SQL_6 = "select strftime('%Y-%m-%d',startDate) as date from table group by strftime('%Y-%m-%d',startDate) order by startDate desc"
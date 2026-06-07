package com.example.test.service.Payment;

//import com.example.test.Entity.Payment.WebhookLog;
//import com.example.test.Repository.Payment.WebhookLogRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;

public class WebhookLogAnalyticsServiceTest {

//    @JavaDispatcher.Container
//    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
//            .withDatabaseName("testdb")
//            .withUsername("test")
//            .withPassword("test");
//
//    // ربط سبرينغ بوت تلقائياً بالقاعدة الحقيقية المنبثقة داخل Docker
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", mysql::getJdbcUrl);
//        registry.add("spring.datasource.username", mysql::getUsername);
//        registry.add("spring.datasource.password", mysql::getPassword);
//        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
//    }
//
//    @Autowired
//    private WebhookLogRepository webhookLogRepository;
//
//    @Autowired
//    private WebhookLogAnalyticsService analyticsService;
//
//    @BeforeEach
//    void setUp() {
//        webhookLogRepository.deleteAll(); // تنظيف قاعدة البيانات الحقيقية المؤقتة
//    }
//
//    @Test
//    @DisplayName("فحص حقيقي متكامل لحساب إحصائيات اليوم في قاعدة بيانات حقيقية")
//    void dailyAnalyticsRealTest() {
//        // ترتيب البيانات بدقة زمانية حقيقية تتوافق مع قاعدة البيانات
//        Instant now = Instant.now();
//
//        WebhookLog log1 = new WebhookLog();
//        log1.setProcessed(true);
//        log1.setCreatedAt(now.minus(2, ChronoUnit.HOURS)); // داخل نطاق الـ 24 ساعة حتماً
//        log1.setEventCategory("payment-intent");
//        log1.setEventId("real_evt_1");
//
//        WebhookLog log2 = new WebhookLog();
//        log2.setProcessed(false);
//        log2.setCreatedAt(now.minus(5, ChronoUnit.HOURS)); // داخل نطاق الـ 24 ساعة حتماً
//        log2.setEventCategory("payment-intent");
//        log2.setEventId("real_evt_2");
//
//        // الحفظ الفعلي في جداول حقيقية وفحص القيود
//        webhookLogRepository.saveAll(List.of(log1, log2));
//
//        // التنفيذ وقراءة البيانات عبر استعلامات الـ SQL الحقيقية
//        Map<String, Long> analytics = analyticsService.dailyAnalytics();
//
//        // التحقق الصارم من النتائج القادمة من قاعدة البيانات
//        assertThat(analytics.get("processed")).isEqualTo(1L);
//        assertThat(analytics.get("failed")).isEqualTo(1L);
//        assertThat(analytics.get("total")).isEqualTo(2L);
//    }
}

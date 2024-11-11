# PsyConnect Project

## Giới thiệu
PsyConnect là một ứng dụng Java Spring Boot được thiết kế để hỗ trợ kết nối và quản lý thông tin giữa các chuyên gia tâm lý và người dùng. Dưới đây là mô tả các packages và chức năng chính của từng thành phần trong project.

## Cấu trúc Project

### `common`
- Chứa các tiện ích, hằng số và các thành phần dùng chung cho toàn bộ project. Các lớp trong package này giúp giảm bớt sự lặp lại mã và tăng tính nhất quán trong ứng dụng.

### `components`
- Bao gồm các thành phần (components) có thể được sử dụng lại trong ứng dụng. Đây là nơi định nghĩa các lớp có khả năng tái sử dụng và đóng vai trò là phần cấu thành chính của ứng dụng.

### `config`
- Package này chứa các file cấu hình cho ứng dụng, bao gồm:
    - **SecurityConfig**: Cấu hình bảo mật cho ứng dụng.
    - **DatabaseConfig**: Cấu hình kết nối và thông tin liên quan đến cơ sở dữ liệu.
    - **CloudinaryConfig**: Cấu hình cho dịch vụ lưu trữ Cloudinary để lưu trữ và quản lý hình ảnh.

### `controllers`
- Chứa các lớp điều khiển (controller) xử lý các request từ client, ánh xạ URL tới các phương thức cụ thể để xử lý logic của ứng dụng. Các lớp trong package này đóng vai trò là cầu nối giữa phần frontend và backend.

### `dto`
- Chứa các lớp Data Transfer Object (DTO) dùng để truyền dữ liệu giữa các tầng của ứng dụng. DTO giúp chuẩn hóa dữ liệu trước khi gửi tới client hoặc từ client tới server.

### `entities`
- Package này chứa các lớp mô hình thực thể (entity) đại diện cho các bảng trong cơ sở dữ liệu. Mỗi lớp tương ứng với một bảng và có các thuộc tính tương ứng với các cột trong bảng.

### `exceptions`
- Chứa các lớp xử lý ngoại lệ tùy chỉnh của ứng dụng. Khi có lỗi xảy ra, các ngoại lệ trong package này sẽ giúp cung cấp thông báo lỗi chi tiết và dễ hiểu hơn cho người dùng.

### `repositories`
- Chứa các interface Repository (kho lưu trữ) cho phép thực hiện các thao tác CRUD (Create, Read, Update, Delete) với cơ sở dữ liệu. Các repository này sử dụng Spring Data JPA để tương tác với database một cách dễ dàng.

### `responses`
- Chứa các lớp định nghĩa các phản hồi (response) trả về từ server tới client. Các lớp trong package này giúp chuẩn hóa dữ liệu trả về và cải thiện trải nghiệm người dùng.

### `services`
- Package này chứa các lớp dịch vụ (service) để xử lý logic nghiệp vụ của ứng dụng. Các lớp service này gọi đến repository để lấy dữ liệu và xử lý logic phức tạp trước khi trả về cho controller.

### `PsyConnectApplication`
- File chính để khởi động ứng dụng. File này chứa phương thức `main()` khởi chạy ứng dụng Spring Boot.

## Thư mục `resources`

### `db.migration`
- Chứa các file migration cho cơ sở dữ liệu, dùng để quản lý phiên bản của schema và thực hiện các thay đổi cơ sở dữ liệu khi ứng dụng phát triển.

### `static`
- Chứa các tài nguyên tĩnh như hình ảnh, CSS, JavaScript được phục vụ trực tiếp tới client.

### `templates`
- Chứa các file HTML template dùng cho email hoặc giao diện. Các file này có thể được sử dụng để gửi email tùy chỉnh hoặc cho các trang web động.

### `application.properties`
- File cấu hình chính của ứng dụng, chứa các thông tin cấu hình như kết nối cơ sở dữ liệu, thông tin bảo mật, và các thiết lập cấu hình khác.

---


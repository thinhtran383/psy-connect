package online.thinhtran.psyconnect.controllers;

import lombok.RequiredArgsConstructor;
import online.thinhtran.psyconnect.responses.Response;
import online.thinhtran.psyconnect.responses.statistical.DoctorAndPatientResponse;
import online.thinhtran.psyconnect.responses.users.doctor.DoctorDetailResponse;
import online.thinhtran.psyconnect.responses.users.doctor.DoctorInfoResponse;
import online.thinhtran.psyconnect.services.StatisticalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.base-path}/statistical")
@RequiredArgsConstructor
public class StatisticalController {
    private final StatisticalService statisticalService;

    @GetMapping("/number-users")
    public ResponseEntity<Response<DoctorAndPatientResponse>> getDoctorAndPatient() {
        return ResponseEntity.ok(Response.<DoctorAndPatientResponse>builder()
                .data(statisticalService.getDoctorAndPatient())
                .message("Get number of doctors and patients successfully")
                .build());
    }

    @GetMapping("/top-5-doctors")
    public ResponseEntity<Response<List<DoctorInfoResponse>>> top5Doctor() {
        return ResponseEntity.ok(Response.<List<DoctorInfoResponse>>builder()
                .data(statisticalService.top5Doctor())
                .message("Get top 5 doctors successfully")
                .build());
    }


}

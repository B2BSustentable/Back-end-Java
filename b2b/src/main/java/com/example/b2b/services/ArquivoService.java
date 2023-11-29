package com.example.b2b.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ArquivoService {

    @Value("${supabase.storage.url}")
    private String supabaseStorageUrl;

    @Value("${supabase.storage.apiKey}")
    private String supabaseApiKey;

    public String enviarImagemParaSupabase(MultipartFile file) throws IOException {
        String storageEndpoint = supabaseStorageUrl + "/" + file.getOriginalFilename();

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(supabaseApiKey);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new HttpEntity<>(file.getBytes(), headers));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(storageEndpoint, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return storageEndpoint; // Retorna a URL do arquivo no Supabase Storage
        } else {
            throw new RuntimeException("Erro ao enviar a imagem para o Supabase.");
        }
    }

    public String uploadImagem(MultipartFile file) throws IOException {
        String url = this.enviarImagemParaSupabase(file);
        return url;
    }
}

package com.luizalabs.infrastructure.eai;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
public class FeignClientErrorDecoder implements ErrorDecoder {

  private final ErrorDecoder decoder = new ErrorDecoder.Default();

  @Override
  public Exception decode(String methodKey, Response response) {
    var status = HttpStatus.valueOf(response.status());

    if (status.is4xxClientError()) {
      log.error(
          "INTEGRATION ERROR URL {} STATUS-CODE {}, METHOD_KEY {} ",
          response.request().url(),
          status,
          methodKey);
      return new ResponseStatusException(
          HttpStatus.valueOf(response.status()), "integration filed, because client error.");
    }

    return decoder.decode(methodKey, response);
  }
}

import { HttpClient, HttpResponse, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { firstValueFrom } from 'rxjs';
import { authMockInterceptor } from './auth.mock.interceptor';
import { AuthLoginResponse } from './auth.service';

const AUTH_LOGIN_ENDPOINT = '/api/auth/login';

describe('authMockInterceptor', () => {
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(withInterceptors([authMockInterceptor])), provideHttpClientTesting()],
    });

    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('logs in admin/admin with the same access profile as mmelissa', async () => {
    const adminResponse = await login(' admin ', ' admin ');
    const mmelissaResponse = await login('mmelissa', 'QQww123@456@789');

    expect(adminResponse.status).toBe(200);
    expect(adminResponse.body).toEqual({
      token: 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIkVVTV9aUkQiXSwiZXhwIjoxNzE2NjUwMDAwfQ.fake',
      refreshToken: 'rt_admin789',
      user: {
        username: 'admin',
        firstName: 'Admin',
        lastName: 'Administrator',
        email: 'admin@eservice.com.pl',
        sellerNumber: null,
        auwId: 67890,
        roles: ['EUM_ZRD'],
      },
    });

    expect(adminResponse.body?.user.roles).toEqual(mmelissaResponse.body?.user.roles);
    expect(adminResponse.body?.user.sellerNumber).toBe(mmelissaResponse.body?.user.sellerNumber);
    expect(adminResponse.body?.user.auwId).toBe(mmelissaResponse.body?.user.auwId);
    expect(adminResponse.body?.user.username).not.toBe(mmelissaResponse.body?.user.username);
    expect(adminResponse.body?.token).not.toBe(mmelissaResponse.body?.token);
  });

  it('keeps the existing mmelissa mock response unchanged', async () => {
    const response = await login('mmelissa', 'QQww123@456@789');

    expect(response.status).toBe(200);
    expect(response.body).toEqual({
      token: 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtbWVsaXNzYSIsInJvbGVzIjpbIkVVTV9aUkQiXSwiZXhwIjoxNzE2NjUwMDAwfQ.fake',
      refreshToken: 'rt_xyz789',
      user: {
        username: 'mmelissa',
        firstName: 'Małgorzata',
        lastName: 'Melissa',
        email: 'malgorzata.melissa@eservice.com.pl',
        sellerNumber: null,
        auwId: 67890,
        roles: ['EUM_ZRD'],
      },
    });
  });

  function login(username: string, password: string): Promise<HttpResponse<AuthLoginResponse>> {
    const response$ = httpClient.post<AuthLoginResponse>(
      AUTH_LOGIN_ENDPOINT,
      { username, password },
      { observe: 'response' },
    );

    return firstValueFrom(response$).then((response) => {
      httpTestingController.expectNone(AUTH_LOGIN_ENDPOINT);
      return response;
    });
  }
});

import { AuthService, User } from './auth.service';

describe('AuthService', () => {
  const storageKey = 'auth.currentUser';
  const persistedUser: User = {
    username: 'admin',
    name: 'Administrator',
    role: 'EUM_ADMINISTRATOR',
    email: 'admin@eumowy.local',
    phone: '',
    auwId: 1,
  };

  afterEach(() => {
    localStorage.clear();
  });

  it('logs in with static admin credentials and persists the user', async () => {
    const service = new AuthService();

    const user = await service.login('admin', 'admin');

    expect(user).toEqual(persistedUser);
    expect(service.user).toEqual(persistedUser);
    expect(service.isLoggedIn).toBeTrue();
    expect(localStorage.getItem(storageKey)).toBe(JSON.stringify(persistedUser));
  });

  it('rejects invalid credentials and clears any authenticated state', async () => {
    localStorage.setItem(storageKey, JSON.stringify(persistedUser));
    const service = new AuthService();

    await expectAsync(service.login('user', 'wrong')).toBeRejectedWithError(
      Error,
      'Błędny login lub hasło.',
    );

    expect(service.user).toBeNull();
    expect(service.isLoggedIn).toBeFalse();
    expect(localStorage.getItem(storageKey)).toBeNull();
  });

  it('hydrates a persisted user from localStorage on creation', () => {
    localStorage.setItem(storageKey, JSON.stringify(persistedUser));

    const service = new AuthService();

    expect(service.user).toEqual(persistedUser);
    expect(service.isLoggedIn).toBeTrue();
  });

  it('ignores malformed persisted auth data', () => {
    localStorage.setItem(storageKey, '{malformed-json');

    const service = new AuthService();

    expect(service.user).toBeNull();
    expect(service.isLoggedIn).toBeFalse();
    expect(localStorage.getItem(storageKey)).toBeNull();
  });

  it('clears in-memory and persisted auth state on logout', async () => {
    const service = new AuthService();
    await service.login('admin', 'admin');

    service.logout();

    expect(service.user).toBeNull();
    expect(service.isLoggedIn).toBeFalse();
    expect(localStorage.getItem(storageKey)).toBeNull();
  });
});

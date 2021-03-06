/*
 * http://www.google.com/codesearch/p?hl=fr#WvCCWA-oRmM/trunk/nacl/googleclient/native_client/service_runtime/benchmark/switch.c&q=switch&exact_package=http://nativeclient.googlecode.com/svn&sa=N&cd=1&ct=rc
 */
int main(int ac,
         char **av)
{
  int                   opt;
  int                   err;
  uint16_t              des_s, gs, cs;  /* ds=es=fs */
  Elf32_Phdr            ep;
  uint16_t              ldt_ix;

  NaClAllModulesInit();
  while ((opt = getopt(ac, av, "n:v")) != -1) {
    switch (opt) {
      case 'n':
        num_syscalls = strtoul(optarg, (char **) 0, 0);
        break;
      case 'v':
        NaClLogIncrVerbosity();
        break;
      default:
        fprintf(stderr, "Usage: switch [-n num_syscalls]\n");
        return -1;
    }
  }

  des_s = NaClLdtAllocateSelector(NACL_LDT_DESCRIPTOR_DATA,
                                  0,
                                  (void *) 0,
                                  kNumPagesMinus1);
#if 0
  /* don't know what values to use! */
  gs = NaClLdtAllocateSelector(NACL_LDT_DESCRIPTOR_DATA,
                               0,
                               (void *) 0,
                               kNumPagesMinus1);
#else
  gs = NaClGetGs();
#endif
  cs = NaClLdtAllocateSelector(NACL_LDT_DESCRIPTOR_CODE,
                               0,
                               (void *) 0,
                               kNumPagesMinus1);

  printf("des = 0x%x, gs = 0x%x, cs = 0x%x\n", des_s, gs, cs);

  /*
   * all three selectors give the entire address space, so should have
   * no effect.
   */
  ldt_ix = gs >> 3;

  printf("ldx_ix = 0x%x\n", ldt_ix);

  printf("nacl_thread[%d] = 0x%08x\n", ldt_ix, (uintptr_t) nacl_thread[ldt_ix]);
  printf("nacl_user[%d] = 0x%08x\n", ldt_ix, (uintptr_t) nacl_user[ldt_ix]);
  printf("nacl_sys[%d] = 0x%08x\n", ldt_ix, (uintptr_t) nacl_sys[ldt_ix]);

  /* fill in NaClApp object enough for code to work... */
  NaClAppCtor(&na);
#if 0
  na.mem_start = (uintptr_t) malloc(8 * 1024);
#else
  if (0 != (err = posix_memalign((void **) &na.mem_start, 4 << 12, 8 << 12))) {
    errno = err;
    perror("posix_memalign");
    return 1;
  }
  if (0 != mprotect((void *) na.mem_start,
                    8 << 12,
                    PROT_READ | PROT_EXEC | PROT_WRITE)) {
    perror("mprotect");
    return 1;
  }
#endif
  na.phdrs = &ep;

  na.xlate_base = 0;

  /* generate trampoline thunk */
  NaClLoadTrampoline(&na);
  NaClLoadSpringboard(&na);

  syscall_tbl = (uintptr_t) na.mem_start;
  /* start of trampoline */

  printf("initializing main thread\n");
  printf("ds = es = ss = 0x%x\n", des_s);
  printf("gs = 0x%x\n", gs);
  printf("cs = 0x%x\n", cs);

  NaClAppPrepareToLaunch(&na);
  printf("after prepare to launch:\n");
  printf("ds = es = ss = 0x%x\n", des_s);
  printf("gs = 0x%x\n", gs);
  printf("cs = 0x%x\n", cs);

  na.code_seg_sel = cs;
  na.data_seg_sel = des_s;

  printf("na.code_seg_sel = 0x%02x\n", na.code_seg_sel);
  printf("na.data_seg_sel = 0x%02x\n", na.data_seg_sel);
  printf("&na.code_seg_sel = 0x%08x\n", (uintptr_t) &na.code_seg_sel);
  printf("&na.data_seg_sel = 0x%08x\n", (uintptr_t) &na.data_seg_sel);
  printf("&nat = 0x%08x\n", (uintptr_t) &nat);
  printf("&na = 0x%08x\n", (uintptr_t) &na);

  NaClAppThreadCtor(&nat,
                    &na,
                    (uintptr_t) app_thread,
                    ((uintptr_t) (&stack + 1)) - 128,
                    gs);

  printf("waiting for app thread to finish\n");

  NaClMutexLock(&nat.mu);
  while (nat.state != NACL_APP_THREAD_DEAD) {
    NaClCondVarWait(&nat.cv, &nat.mu);
  }
  NaClMutexUnlock(&nat.mu);

  printf("all done\n");
  printf("freeing posix_memaligned memory\n");
  free((void *) na.mem_start);
  na.mem_start = 0;
  printf("calling dtor\n");
  NaClAppDtor(&na);
  printf("calling fini\n");
  NaClAllModulesFini();
  printf("quitting\n");

  return 0;
}

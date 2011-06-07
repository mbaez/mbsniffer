#include <linux/module.h>
#include <linux/vermagic.h>
#include <linux/compiler.h>

MODULE_INFO(vermagic, VERMAGIC_STRING);

struct module __this_module
__attribute__((section(".gnu.linkonce.this_module"))) = {
 .name = KBUILD_MODNAME,
 .init = init_module,
#ifdef CONFIG_MODULE_UNLOAD
 .exit = cleanup_module,
#endif
 .arch = MODULE_ARCH_INIT,
};

static const struct modversion_info ____versions[]
__used
__attribute__((section("__versions"))) = {
	{ 0xa7672d5a, "module_layout" },
	{ 0x20623cac, "kmalloc_caches" },
	{ 0xdca0e950, "genl_register_family" },
	{ 0x49439411, "genl_unregister_family" },
	{ 0x105e2727, "__tracepoint_kmalloc" },
	{ 0xb72397d5, "printk" },
	{ 0xb4390f9a, "mcount" },
	{ 0xf9ff8b50, "dev_remove_pack" },
	{ 0x6eb25b1e, "kmem_cache_alloc" },
	{ 0x99e05b8f, "kfree_skb" },
	{ 0xd4e14fd2, "genl_register_ops" },
	{ 0x81da75c1, "dev_add_pack" },
};

static const char __module_depends[]
__used
__attribute__((section(".modinfo"))) =
"depends=";


MODULE_INFO(srcversion, "C521D82B8FEA601E90D4D53");

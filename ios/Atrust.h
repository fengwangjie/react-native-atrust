
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNAtrustSpec.h"

@interface Atrust : NSObject <NativeAtrustSpec>
#else
#import <React/RCTBridgeModule.h>

@interface Atrust : NSObject <RCTBridgeModule>
#endif

@end

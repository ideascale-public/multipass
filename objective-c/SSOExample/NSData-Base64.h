//
//  NSData-Base64.h
//  Klip
//
//  Created by xiangdong liu on 6/22/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface NSData (NSData_Base64)
+ (NSString*) encodeKLBase64:(NSData*) rawBytes;

//nodecode needed
@end

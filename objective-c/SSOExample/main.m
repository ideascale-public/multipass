//
//  main.m
//  SSOExample
//
//  Created by xiangdong liu on 8/16/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SSOHelper.h"





int main (int argc, const char * argv[])
{

  NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];

  NSString* sso  = [SSOHelper getSSOWithEmail:@"objectivec@domain.com" DisplayName:@"objectivec domain"]; 
  NSLog(@"http://multipass.ideascale.com/a/panel.do?multipass=%@",sso);

  [pool drain];
    return 0;
}


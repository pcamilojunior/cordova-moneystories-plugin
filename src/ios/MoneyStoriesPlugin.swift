//
// MoneyStoriesPlugin.swift
//
// Created by Paulo Cesar Camilo.
//
import Foundation
import MoneyStories
@objc(MoneyStoriesPlugin)
class MoneyStoriesPlugin: CDVPlugin {

  @objc(initializeSdk:)
  func initializeSdk (_ command: CDVInvokedUrlCommand){
    if let initArguments = command.arguments[0] as? NSDictionary {
      if let baseURL = initArguments["baseUrl"] as? String {
        if let languageCode = initArguments["languageCode"] as? String {
          if let token = initArguments["accessToken"] as? String{
            if let customerId = initArguments["customerId"] as? String {
              /**              moneyStories().setup(withConfigBuilder: ConfigBuilder()
                    .withDebugEnabled()
                    .withBaseUrl(url: URL(string: baseURL))
                    .withLanguageCode(code: languageCode)
                  )
                */
            } else {
              //Callback
            }
          } else {
            //Callback
          }
        } else {
          //Callback
        }
      } else {
        //Callback
      }
    } else {
      //Callback Missing Arguments
      sendPluginResult(command: command, status: CDVCommandStatus_ERROR, message: "Error: Missing input parameters")
    }
  }

  @objc(openStories:)
  func openStories(_ command: CDVInvokedUrlCommand){
    /**
     if let item = self.viewModel.storyLines[safe: index] {
       let presenterVC = self.findViewController()
       if let viewerVC = Assembler.syncResolver.resolve(StoryManagerViewController.self, argument: item) {
         viewerVC.modalPresentationStyle = .overFullScreen
         viewerVC.openAction = .storybar // Analytics
         viewerVC.loadViewIfNeeded() // force preload view and call viewdidload
         DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
           presenterVC?.present(viewerVC, animated: true, completion: nil)
         }
       }
     }
      */
  }

  @objc(refreshToken:)
  func refreshToken(_ command: CDVInvokedUrlCommand){

  }

  func sendPluginResult(command: CDVInvokedUrlCommand, status: CDVCommandStatus, message: String = "") {
    var pluginResult = CDVPluginResult()
    pluginResult = CDVPluginResult(status: status, messageAs: message)
    self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
  }
}
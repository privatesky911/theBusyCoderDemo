from django.http import HttpResponse


def helloworld(request):
    print("print hello")
    return HttpResponse("Hello World!")

def hi(self):
    print("hi")
    return HttpResponse("Hi!!")

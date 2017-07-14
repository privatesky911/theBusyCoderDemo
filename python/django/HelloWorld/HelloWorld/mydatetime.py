import datetime

from django.http import HttpResponse


def current_datetime(request):
    now = datetime.datetime.now();
    html = "<html><body>It is now %s.</body></html>" % now
    return HttpResponse(html)


def hours_ahead(request, offset):
    h = int(offset)
    "assert ()"
    dt = datetime.datetime.now() + datetime.timedelta(hours=h)
    html = "<html><body>In %s hours, it will be %s.</body></html>" % (h, dt)
    return HttpResponse(html)

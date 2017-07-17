import datetime

from django.http import HttpResponse
from django.template.response import TemplateResponse

def current_datetime(request):
    now = datetime.datetime.now()
    return TemplateResponse(request, 'current_datetime.html', {'current_date': now}, )


def hours_ahead(request, offset):
    h = int(offset)
    "assert ()"
    dt = datetime.datetime.now() + datetime.timedelta(hours=h)
    html = "<html><body>In %s hours, it will be %s.</body></html>" % (h, dt)
    return HttpResponse(html)
